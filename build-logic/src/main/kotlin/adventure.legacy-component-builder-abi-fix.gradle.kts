import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

// Remove in 6.x.
// In 5.x, ComponentBuilder#build erased from Object to Component, causing NoSuchMethodError for 4.x-compiled clients.
// This adds a build-time classfile bridge for build():Object while keeping the 5.x source API strongly typed.

abstract class PatchComponentBuilderAbi : DefaultTask() {
  @get:InputFile
  abstract val inputClass: RegularFileProperty

  @get:OutputFile
  abstract val outputClass: RegularFileProperty

  @TaskAction
  fun patch() {
    val reader = ClassReader(inputClass.get().asFile.readBytes())
    val writer = ClassWriter(reader, 0)

    var hasComponentBuild = false
    var hasObjectBuild = false

    reader.accept(object : ClassVisitor(Opcodes.ASM9, writer) {
      override fun visitMethod(access: Int, name: String, desc: String, sig: String?, ex: Array<out String>?): MethodVisitor {
        if (name == "build" && desc == "()Lnet/kyori/adventure/text/Component;") hasComponentBuild = true
        if (name == "build" && desc == "()Ljava/lang/Object;") hasObjectBuild = true
        return super.visitMethod(access, name, desc, sig, ex)
      }

      override fun visitEnd() {
        check(hasComponentBuild) { "Missing ComponentBuilder.build():Component" }

        if (!hasObjectBuild) {
          super.visitMethod(
            Opcodes.ACC_PUBLIC or Opcodes.ACC_BRIDGE or Opcodes.ACC_SYNTHETIC,
            "build",
            "()Ljava/lang/Object;",
            null,
            null
          ).apply {
            visitCode()
            visitVarInsn(Opcodes.ALOAD, 0)
            visitMethodInsn(
              Opcodes.INVOKEINTERFACE,
              "net/kyori/adventure/text/ComponentBuilder",
              "build",
              "()Lnet/kyori/adventure/text/Component;",
              true
            )
            visitInsn(Opcodes.ARETURN)
            visitMaxs(1, 1)
            visitEnd()
          }
        }

        super.visitEnd()
      }
    }, 0)

    outputClass.get().asFile.apply {
      parentFile.mkdirs()
      writeBytes(writer.toByteArray())
    }
  }
}

val componentBuilderClass = "net/kyori/adventure/text/ComponentBuilder.class"
val patchedClasses = layout.buildDirectory.dir("patched-component-builder-abi")

val patchComponentBuilderAbi = tasks.register<PatchComponentBuilderAbi>("patchComponentBuilderAbi") {
  description = "Patches the ComponentBuilder class to restore binary compatibility with 4.x"
  val compileJava = tasks.named<JavaCompile>("compileJava")
  inputClass.set(compileJava.flatMap { it.destinationDirectory.file(componentBuilderClass) })
  outputClass.set(patchedClasses.map { it.file(componentBuilderClass) })
  dependsOn(compileJava)
}

tasks.named<Jar>("jar") {
  dependsOn(patchComponentBuilderAbi)

  filesMatching(componentBuilderClass) {
    if (!file.toPath().startsWith(patchedClasses.get().asFile.toPath())) {
      exclude()
    }
  }

  from(patchedClasses) {
    include(componentBuilderClass)
  }
}
