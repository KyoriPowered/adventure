plugins {
  idea
  val indraVersion = "2.0.4"
  id("net.kyori.indra") version indraVersion
  id("net.kyori.indra.publishing.sonatype") version indraVersion
  id("net.kyori.indra.license-header") version indraVersion
  id("net.kyori.indra.checkstyle") version indraVersion
  id("me.champeau.jmh") version "0.6.5"
}

val javacc: Configuration by configurations.creating

group = "net.kyori"
version = "4.2.0-SNAPSHOT"
description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

repositories {
  mavenCentral()
}

dependencies {
  api(libs.adventure.api)
  checkstyle(libs.checkstyle)
  compileOnlyApi(libs.jetbrains.annotations)
  testImplementation(libs.adventure.text.plain)
  testImplementation(libs.adventure.text.gson)
  testImplementation(libs.junit.api)
  testRuntimeOnly(libs.junit.engine)
}

indra {
  javaVersions {
    testWith(8, 11, 16)
  }

  github("KyoriPowered", "adventure-text-minimessage") {
    ci(true)
  }
  mitLicense()

  configurePublications {
    pom {
      developers {
        developer {
          id.set("minidigger")
          name.set("MiniDigger")
        }
      }
    }
  }
}

tasks.checkstyleJmh {
  exclude("**")
}

tasks.jar {
  manifest.attributes(
    "Automatic-Module-Name" to "net.kyori.adventure.text.minimessage"
  )
}
