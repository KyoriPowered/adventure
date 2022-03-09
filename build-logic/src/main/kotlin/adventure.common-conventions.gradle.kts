import com.adarshr.gradle.testlogger.theme.ThemeType
import me.champeau.jmh.JMHPlugin
import me.champeau.jmh.JmhParameters
import org.gradle.api.artifacts.type.ArtifactTypeDefinition

plugins {
  id("adventure.base-conventions")
  id("net.kyori.indra.crossdoc")
  id("net.kyori.indra")
  id("net.kyori.indra.checkstyle")
  id("net.kyori.indra.license-header")
  id("com.adarshr.test-logger")
  id("com.diffplug.eclipse.apt")
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
    jmhVersion.set(libs.versions.jmh.get())
  }
  tasks.named("compileJmhJava") {
    // avoid implicit task dependencies
    dependsOn(tasks.compileTestJava, tasks.processTestResources)
  }
  tasks.named(JMHPlugin.JMH_TASK_COMPILE_GENERATED_CLASSES_NAME, JavaCompile::class) {
    classpath += configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).incoming.files
  }
}

configurations {
  testCompileClasspath {
    exclude(group = "junit") // brought in by google's libs
  }
}

dependencies {
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

val ADVENTURE_PREFIX = "adventure-"
indraCrossdoc {
  baseUrl().set(providers.gradleProperty("javadocPublishRoot"))
  nameBasedDocumentationUrlProvider {
    projectNamePrefix.set(ADVENTURE_PREFIX)
  }
}

tasks {
  javadoc {
    val options = options as? StandardJavadocDocletOptions ?: return@javadoc
    options.tags("sinceMinecraft:a:Since Minecraft:")
  }

  jacocoTestReport {
    dependsOn(test)
  }
}
