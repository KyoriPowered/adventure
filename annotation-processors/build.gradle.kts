plugins {
  id("adventure.common-conventions")
}

dependencies {
  annotationProcessor(libs.autoService.processor)
  compileOnlyApi(libs.autoService.annotations)
  api(libs.jetbrainsAnnotations)
}

tasks.withType(AbstractPublishToMaven::class) {
  isEnabled = false
}
