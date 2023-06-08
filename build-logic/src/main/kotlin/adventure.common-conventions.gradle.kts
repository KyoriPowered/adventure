import com.adarshr.gradle.testlogger.theme.ThemeType
import com.diffplug.gradle.spotless.FormatExtension
import me.champeau.jmh.JMHPlugin
import me.champeau.jmh.JmhParameters
import net.ltgt.gradle.errorprone.errorprone

plugins {
  id("adventure.base-conventions")
  id("net.kyori.indra.crossdoc")
  id("net.kyori.indra")
  id("net.kyori.indra.checkstyle")
  id("net.kyori.indra.licenser.spotless")
  id("com.adarshr.test-logger")
  id("com.diffplug.eclipse.apt")
  id("net.ltgt.errorprone")
  jacoco
}
// expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

testlogger {
  theme = ThemeType.MOCHA_PARALLEL
  showPassed = false
}

plugins.withId("me.champeau.jmh") {
  extensions.configure(JmhParameters::class) {
    jmhVersion = libs.versions.jmh.get()
  }
  tasks.named("compileJmhJava") {
    // avoid implicit task dependencies
    dependsOn(tasks.compileTestJava, tasks.processTestResources)
  }
  tasks.named(JMHPlugin.getJMH_TASK_COMPILE_GENERATED_CLASSES_NAME(), JavaCompile::class) {
    classpath += configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).incoming.files
  }
}

configurations {
  testCompileClasspath {
    exclude(group = "junit") // brought in by google's libs
  }
}

dependencies {
  errorprone(libs.errorprone)
  annotationProcessor(libs.contractValidator) 
  api(platform(project(":adventure-bom")))
  checkstyle(libs.stylecheck)
  testImplementation(libs.guava.testlib)
  testImplementation(libs.truth)
  testImplementation(libs.truth.java8)
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.api)
  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.params)
}

spotless {
  fun FormatExtension.applyCommon() {
    trimTrailingWhitespace()
    endWithNewline()
    indentWithSpaces(2)
  }
  java {
    importOrderFile(rootProject.file(".spotless/kyori.importorder"))
    applyCommon()
  }
  kotlinGradle {
    applyCommon()
  }
}

val ADVENTURE_PREFIX = "adventure-"
indraCrossdoc {
  baseUrl().set(providers.gradleProperty("javadocPublishRoot"))
  nameBasedDocumentationUrlProvider {
    projectNamePrefix = ADVENTURE_PREFIX
  }
}

tasks {
  javadoc {
    val options = options as? StandardJavadocDocletOptions ?: return@javadoc
    options.tags("sinceMinecraft:a:Since Minecraft:")
    options.links(
      "https://javadoc.io/doc/net.kyori/examination-api/${libs.versions.examination.get()}/",
      "https://javadoc.io/doc/net.kyori/examination-string/${libs.versions.examination.get()}/",
      "https://javadoc.io/doc/org.jetbrains/annotations/${libs.versions.jetbrainsAnnotations.get()}/",
    )
  }

  jacocoTestReport {
    dependsOn(test)
  }
  
  withType(JavaCompile::class) {
    options.errorprone {
      disable("InvalidBlockTag") // we use custom block tags
      disable("InlineMeSuggester") // we don't use errorprone annotations
      disable("ReferenceEquality") // lots of comparison against EMPTY objects
      disable("CanIgnoreReturnValueSuggester") // suggests errorprone annotation, not JB Contract annotation
    }
  }
}
