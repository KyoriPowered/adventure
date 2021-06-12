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

configurations.testCompileClasspath {
  exclude(group = "junit") // brought in by google's libs
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
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

tasks {
  javadoc {
    val options = options as? StandardJavadocDocletOptions ?: return@javadoc
    options.tags("sinceMinecraft:a:Since Minecraft:")
  }

  register("copyJavadoc", CopyJavadoc::class) {
    projectName.set(provider { project.name })
    projectVersion.set(provider { project.version.toString() })
    javadocFiles.from(javadoc)
    rootDir.set(project.rootDir)
  }
}
