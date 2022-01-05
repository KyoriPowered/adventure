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

testlogger {
  theme = ThemeType.MOCHA_PARALLEL
  showPassed = false
}

plugins.withId("me.champeau.jmh") {
  extensions.configure(JmhParameters::class) {
    jmhVersion.set(providers.gradleProperty("jmhVersion").forUseAtConfigurationTime())
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

repositories {
  mavenCentral()
}

dependencies {
  annotationProcessor("ca.stellardrift:contract-validator:1.0.1") // https://github.com/zml2008/contract-validator
  api(platform(project(":adventure-bom")))
  checkstyle("ca.stellardrift:stylecheck:0.1")
  testImplementation("com.google.guava:guava-testlib:31.0.1-jre")
  testImplementation("com.google.truth:truth:1.1.3")
  testImplementation("com.google.truth.extensions:truth-java8-extension:1.1.3")
  testImplementation(platform("org.junit:junit-bom:5.8.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
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
