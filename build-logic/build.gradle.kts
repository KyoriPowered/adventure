plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  val indraVersion = "2.0.6"
  implementation("net.kyori", "indra-common", indraVersion)
  implementation("net.kyori", "indra-publishing-sonatype", indraVersion)
  implementation("com.adarshr", "gradle-test-logger-plugin", "3.0.0")
  implementation("me.champeau.jmh", "jmh-gradle-plugin", "0.6.5")
  implementation("com.diffplug.gradle", "goomph", "3.31.0")
}
