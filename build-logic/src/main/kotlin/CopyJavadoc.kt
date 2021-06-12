import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import javax.inject.Inject

internal const val ADVENTURE_PREFIX = "adventure-"

/**
 * Copy project javadoc into the `adventure-javadoc` directory tree
 */
abstract class CopyJavadoc : DefaultTask() {
  @get:Input
  abstract val projectName: Property<String>

  @get:Input
  abstract val projectVersion: Property<String>

  @get:InputFiles
  abstract val javadocFiles: ConfigurableFileCollection

  @get:Internal
  abstract val rootDir: DirectoryProperty

  @get:Internal
  @get:Option(option="output", description="The root of the adventure-javadocs repository")
  abstract val output: Property<String>

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @get:Inject
  protected abstract val fsOps: FileSystemOperations

  init {
    // relative to project root, <output>/<projectName>/<projectVersion>
    outputDir.set(rootDir.dir(output).flatMap { it.dir(this.projectName).flatMap { it.dir(this.projectVersion) } })
  }

  @TaskAction
  fun doTransfer() {
    val dest = outputDir.get().asFile

    dest.deleteRecursively()
    dest.mkdirs()

    fsOps.copy {
      from(javadocFiles)
      into(dest)
    }
  }
}
