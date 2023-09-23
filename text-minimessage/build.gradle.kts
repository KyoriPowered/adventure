plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

dependencies {
  api(projects.adventureApi)
  testImplementation(project(":adventure-text-serializer-plain"))
  annotationProcessor(projects.adventureAnnotationProcessors)
}

indra.javaVersions {
  testWith(21)
}

sourceSets {
  main {
    multirelease.alternateVersions(21)
  }
  test {
    multirelease.alternateVersions(21)
  }
}

afterEvaluate {
  tasks.named("compileJava21Java", JavaCompile::class) {
    options.compilerArgs = options.compilerArgs + listOf("--enable-preview")
  }
  tasks.named("compileTestJava21Java", JavaCompile::class) {
    options.compilerArgs = options.compilerArgs + listOf("--enable-preview")
  }
  tasks.named("testJava21", Test::class) {
    jvmArgs("--enable-preview")
  }
  if (JavaVersion.current() >= JavaVersion.VERSION_21) {
    tasks.test {
      jvmArgs("--enable-preview")
    }
  }
  dependencies {
    "testJava21Implementation"(sourceSets.named("java21").map { it.output })
  }
  tasks.named("checkstyleTestJava21", Checkstyle::class) {
    isEnabled = false // parser issue with J21 syntax
  }
}

tasks.checkstyleJmh {
  exclude("**")
}

tasks.javadoc {
  exclude("net/kyori/adventure/text/minimessage/internal/**")
}

applyJarMetadata("net.kyori.adventure.text.minimessage")
