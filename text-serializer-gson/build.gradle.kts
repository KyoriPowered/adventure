plugins {
  id("adventure.json-impl-conventions")
  alias(libs.plugins.jmh)
}

dependencies {
  api(libs.gson)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson")
