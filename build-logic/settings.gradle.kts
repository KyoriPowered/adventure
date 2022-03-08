rootProject.name = "adventure-build-logic"

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    maven(url = "https://repo.stellardrift.ca/repository/internal/") {
      name = "stellardriftReleases"
      mavenContent { releasesOnly() }
    }
    maven(url = "https://repo.stellardrift.ca/repository/snapshots/") {
      name = "stellardriftSnapshots"
      mavenContent { snapshotsOnly() }
    }
  }
  
  versionCatalogs {
    register("libs") {
      from(files("../gradle/libs.versions.toml")) // include from parent project
    }
  }
}