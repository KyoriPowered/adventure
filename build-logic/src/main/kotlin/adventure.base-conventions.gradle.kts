plugins {
  id("net.kyori.indra.publishing")
}

indra {
  javaVersions {
    testWith(8, 11, 17)
  }

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
