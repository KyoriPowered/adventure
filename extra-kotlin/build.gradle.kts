plugins {
  id("adventure.common-conventions")
  kotlin("jvm") version "1.5.10"
}

kotlin {
  coreLibrariesVersion = "1.4.32"
  target {
    compilations.configureEach {
        kotlinOptions {
          jvmTarget = "1.8"
          languageVersion = "1.4"
        }
    }
  }
}

dependencies {
  api(project(":adventure-api"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

applyJarMetadata("net.kyori.adventure.extra.kotlin")
