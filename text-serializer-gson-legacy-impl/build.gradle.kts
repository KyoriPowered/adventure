plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureTextSerializerGson)
  api(projects.adventureTextSerializerJsonLegacyImpl)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson.legacyimpl")
