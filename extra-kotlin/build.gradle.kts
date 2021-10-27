plugins {
  id("adventure.common-conventions")
  kotlin("jvm") version "1.5.31"
}

kotlin {
  explicitApi()
  coreLibrariesVersion = "1.5.31"
  target {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.5"
      }
    }
  }
}

dependencies {
  api(project(":adventure-api"))
  implementation(kotlin("stdlib-jdk8"))
  testImplementation(kotlin("test-junit5"))
}

applyJarMetadata("net.kyori.adventure.extra.kotlin")
