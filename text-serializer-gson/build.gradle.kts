plugins {
  id("adventure.json-impl-conventions")
  alias(libs.plugins.jmh)
}

dependencies {
  api(libs.gson)
  implementation(projects.adventureTextSerializerCommons)
  compileOnlyApi(libs.autoService.annotations)
  annotationProcessor(libs.autoService)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson")
