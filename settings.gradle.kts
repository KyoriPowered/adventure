pluginManagement {
  includeBuild("build-logic")
  repositories {
    maven(url = "https://repo.stellardrift.ca/repository/internal/") {
      name = "stellardriftReleases"
      mavenContent { releasesOnly() }
    }
    maven(url = "https://repo.stellardrift.ca/repository/snapshots/") {
      name = "stellardriftSnapshots"
      mavenContent { snapshotsOnly() }
    }
    gradlePluginPortal()
  }
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
  "bom",
  "extra-kotlin",
  "key",
  "nbt",
  "serializer-configurate3",
  "serializer-configurate4",
  "text-logger-slf4j",
  "text-minimessage",
  "text-serializer-gson",
  "text-serializer-gson-legacy-impl",
  "text-serializer-legacy",
  "text-serializer-plain"
).forEach {
  include("adventure-$it")
  project(":adventure-$it").projectDir = file(it)
}
