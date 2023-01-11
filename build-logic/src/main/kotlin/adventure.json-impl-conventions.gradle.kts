plugins {
  id("adventure.common-conventions")
}

val sharedTests by configurations.registering {
  isCanBeResolved = false
  isCanBeConsumed = false
}

val sharedTestDirs by configurations.registering {
  extendsFrom(sharedTests.get())
  isTransitive = false // we want the directory on its own

  attributes {
    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API)) // needs to be API to get the unpacked test-fixtures variant
    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
  }
}

configurations.testImplementation {
  extendsFrom(sharedTests.get())
}

tasks.test {
  testClassesDirs += sharedTestDirs.get()
}

dependencies {
  val textSerializerJson = project(":adventure-text-serializer-json")
  api(textSerializerJson)
  sharedTests.name(testFixtures(textSerializerJson))
  annotationProcessor(project(":adventure-annotation-processors"))
}
