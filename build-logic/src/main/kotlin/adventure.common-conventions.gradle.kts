import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
  id("adventure.base-conventions")
  id("net.kyori.indra")
  id("net.kyori.indra.checkstyle")
  id("net.kyori.indra.license-header")
  id("com.adarshr.test-logger")
}

testlogger {
  theme = ThemeType.MOCHA_PARALLEL
  showPassed = false
}

configurations {
  testCompileClasspath {
    exclude(group = "junit") // brought in by google's libs
  }

  // Register unpacked Javadoc as an artifact for cross-linking
  javadocElements {
    outgoing {
      variants {
        create("files") {
          artifact(tasks.javadoc.map { it.destinationDir!! }) {
            builtBy(tasks.javadoc)
          }
          attributes {
            attribute(JavadocPackaging.ATTRIBUTE, objects.named(JavadocPackaging.DIRECTORY))
          }
        }
      }
    }
  }
}

// Resolve JD for cross-linking between modules
val offlineLinkedJavadoc = configurations.register("offlineLinkedJavadoc") {
  isCanBeResolved = true
  isCanBeConsumed = false

  attributes {
    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.JAVADOC))
    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    attribute(JavadocPackaging.ATTRIBUTE, objects.named(JavadocPackaging.DIRECTORY))
  }
  extendsFrom(configurations.runtimeElements.get())
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  attributesSchema {
    attribute(JavadocPackaging.ATTRIBUTE)
  }

  annotationProcessor("ca.stellardrift:contract-validator:1.0.0") // https://github.com/zml2008/contract-validator
  api(platform(project(":adventure-bom")))
  checkstyle("ca.stellardrift:stylecheck:0.1")
  testImplementation("com.google.guava:guava-testlib:30.1.1-jre")
  testImplementation("com.google.truth:truth:1.1.3")
  testImplementation("com.google.truth.extensions:truth-java8-extension:1.1.3")
  testImplementation(platform("org.junit:junit-bom:5.7.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
}

fun filterProjectName(name: String): String {
  return if (name.startsWith(ADVENTURE_PREFIX)) {
    name.substring(ADVENTURE_PREFIX.length, name.length)
  } else {
    name
  }
}

tasks {
  // link to modules in project
  val submoduleLinkRoot = providers.gradleProperty("javadocPublishRoot")
  val jdLinks = offlineLinkedJavadoc.get().incoming
    .artifactView {
      componentFilter { it is ProjectComponentIdentifier } // only in-project
      isLenient = true // ignore artifacts with no javadoc elements variant
    }
  val version = project.version
  javadoc {
    inputs.property("jdLinks.root", submoduleLinkRoot)
    inputs.property("project.version", version)
    inputs.files(jdLinks.files)
      .ignoreEmptyDirectories()
      .withPropertyName("jdLinks.directories")

    val options = options as? StandardJavadocDocletOptions ?: return@javadoc
    options.tags("sinceMinecraft:a:Since Minecraft:")

    doFirst {
      jdLinks.artifacts.forEach {
        val file = it.file
        val projectName = (it.id.componentIdentifier as ProjectComponentIdentifier).projectName
        if (!file.isDirectory) {
          logger.warn("Failed to link to Javadoc in $file (for $projectName) because it was not a directory")
        }

        // This matches the file structure in adventure-javadocs
        var linkRoot = submoduleLinkRoot.get()
        if (!linkRoot.endsWith("/")) {
          linkRoot += "/"
        }

        options.linksOffline("${linkRoot}${filterProjectName(projectName)}/$version", file.absolutePath)
      }
    }
  }

  register("copyJavadoc", CopyJavadoc::class) {
    projectName.set(provider { filterProjectName(project.name) })
    projectVersion.set(provider { project.version.toString() })
    javadocFiles.from(javadoc)
    rootDir.set(project.rootDir)
  }
}
