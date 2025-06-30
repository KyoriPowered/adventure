plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.option)
  api(projects.adventureApi)
  api(projects.adventureNbt)
  compileOnlyApi(libs.autoService.annotations)
  implementation(projects.adventureTextSerializerCommons)
  annotationProcessor(libs.autoService)
}

applyJarMetadata("net.kyori.adventure.text.serializer.nbt")
