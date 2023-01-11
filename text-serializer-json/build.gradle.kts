plugins {
  id("adventure.common-conventions")
  `java-test-fixtures`
}

dependencies {
  api(projects.adventureApi)
  annotationProcessor(projects.adventureAnnotationProcessors)
  testFixturesApi(libs.junit.api)
  testFixturesApi(platform(libs.junit.bom))
}

applyJarMetadata("net.kyori.adventure.text.serializer.json")

(components["java"] as AdhocComponentWithVariants).run {
  withVariantsFromConfiguration(configurations.testFixturesApiElements.get()) { skip() }
  withVariantsFromConfiguration(configurations.testFixturesRuntimeElements.get()) { skip() }
}
