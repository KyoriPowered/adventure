pluginManagement {
  includeBuild("build-logic")
  repositories {
    maven(url = "https://repo.stellardrift.ca/maven/internal/") {
      name = "stellardriftReleases"
      mavenContent { releasesOnly() }
    }
    maven(url = "https://repo.papermc.io/repository/maven-snapshots/") {
      name = "papermcSnapshots"
      mavenContent { snapshotsOnly() }
    }
    maven(url = "https://repo.stellardrift.ca/maven/snapshots/") {
      name = "stellardriftSnapshots"
      mavenContent { snapshotsOnly() }
    }
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
  }
}

rootProject.name = "adventure-parent"

// Make sure to update bom/build.gradle.kts when making changes to modules.

sequenceOf(
  "api",
  "annotation-processors",
  "bom",
  "javadoc",
  "key",
  "nbt",
  "serializer-configurate4",
  "text-logger-slf4j",
  "text-minimessage",
  "text-serializer-commons",
  "text-serializer-gson",
  "text-serializer-json",
  "text-serializer-json-legacy-impl",
  "text-serializer-legacy",
  "text-serializer-plain",
  "text-serializer-ansi",
  "text-serializer-nbt"
).forEach {
  include("adventure-$it")
  project(":adventure-$it").projectDir = file(it)
}
