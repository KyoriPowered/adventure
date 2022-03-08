plugins {
  id("net.kyori.indra.publishing")
}

// expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

indra {
  javaVersions {
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

  configurePublications {
    pom {
      developers {
        developer {
          id.set("kashike")
          timezone.set("America/Vancouver")
        }

        developer {
          id.set("lucko")
          name.set("Luck")
          url.set("https://lucko.me")
          email.set("git@lucko.me")
        }

        developer {
          id.set("zml")
          name.set("zml")
          timezone.set("America/Vancouver")
        }

        developer {
          id.set("Electroid")
        }

        developer {
          id.set("minidigger")
          name.set("MiniDigger")
        }

        developer {
          id.set("kezz")
        }

        developer {
          id.set("broccolai")
        }

        developer {
          id.set("rymiel")
        }
      }
    }
  }
}
