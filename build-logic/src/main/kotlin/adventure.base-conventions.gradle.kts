plugins {
  id("net.kyori.indra.publishing")
}

// expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

indra {
  javaVersions {
    minimumToolchain(17)
    val testVersions = (project.property("testJdks") as String)
      .split(",")
      .map { it.trim().toInt() }
    testWith().addAll(testVersions)
  }
  checkstyle(libs.versions.checkstyle.get())

  github("KyoriPowered", "adventure") {
    ci(true)
  }
  mitLicense()

  signWithKeyFromPrefixedProperties("kyori")
  configurePublications {
    pom {
      developers {
        developer {
          id = "kashike"
          timezone = "America/Vancouver"
        }

        developer {
          id = "lucko"
          name = "Luck"
          url = "https://lucko.me"
          email = "git@lucko.me"
        }

        developer {
          id = "zml"
          name = "zml"
          timezone = "America/Vancouver"
        }

        developer {
          id = "Electroid"
        }

        developer {
          id = "minidigger"
          name = "MiniDigger"
        }

        developer {
          id = "kezz"
        }

        developer {
          id = "broccolai"
        }

        developer {
          id = "rymiel"
        }
      }
    }
  }
}
