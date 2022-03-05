plugins {
  `kotlin-dsl`
}

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

dependencies {
  val indraVersion = "2.1.1"
  implementation("net.kyori", "indra-common", indraVersion)
  implementation("net.kyori", "indra-publishing-sonatype", indraVersion)
  implementation("net.kyori", "indra-crossdoc", indraVersion)
  implementation("com.adarshr", "gradle-test-logger-plugin", "3.2.0")
  implementation("me.champeau.jmh", "jmh-gradle-plugin", "0.6.6")
  implementation("com.diffplug.gradle", "goomph", "3.35.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}
