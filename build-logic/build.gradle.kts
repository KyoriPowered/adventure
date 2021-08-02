plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  val indraVersion = "2.0.5"
  implementation("net.kyori", "indra-common", indraVersion)
  implementation("net.kyori", "indra-publishing-sonatype", indraVersion)
  implementation("com.adarshr", "gradle-test-logger-plugin", "3.0.0")
  implementation("me.champeau.jmh", "jmh-gradle-plugin", "0.6.5")
}
