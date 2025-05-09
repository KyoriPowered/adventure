import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.kotlin)
}

kotlin {
  explicitApi()
  coreLibrariesVersion = "1.4.32"
  compilerOptions {
    jvmTarget = JvmTarget.JVM_1_8
    @Suppress("DEPRECATION")
    languageVersion = KotlinVersion.KOTLIN_1_6
  }
}

dependencies {
  api(projects.adventureApi)
  implementation(libs.kotlin.stdlib)
  testImplementation(libs.kotlin.testJunit5)
}

applyJarMetadata("net.kyori.adventure.extra.kotlin")
