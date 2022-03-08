plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.kotlin)
}

kotlin {
  explicitApi()
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
  api(projects.adventureApi)
  implementation(libs.kotlin.stdlib)
  testImplementation(libs.kotlin.testJunit5)
}

applyJarMetadata("net.kyori.adventure.extra.kotlin")
