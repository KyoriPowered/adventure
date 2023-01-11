import me.champeau.jmh.JMHPlugin
import me.champeau.jmh.JmhBytecodeGeneratorTask

plugins {
  id("adventure.common-conventions")
}

val sharedTests by configurations.registering {
  isVisible = false
  isCanBeResolved = false
  isCanBeConsumed = false
}

val sharedTestDirs by configurations.registering {
  isVisible = false
  isCanBeConsumed = false
  extendsFrom(sharedTests.get())
  isTransitive = false // we want the directory on its own

  attributes {
    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API)) // needs to be API to get the unpacked test-fixtures variant
    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
  }
}

val sharedBenchmarks by configurations.registering {
  isVisible = false
  isTransitive = false

  attributes {
    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
  }
}

configurations.testRuntimeOnly {
  extendsFrom(sharedTests.get())
}

tasks.test {
  testClassesDirs += sharedTestDirs.get()
}

dependencies {
  val textSerializerJson = project(":adventure-text-serializer-json")
  api(textSerializerJson)
  sharedTests.name(testFixtures(textSerializerJson.copy()))
  sharedBenchmarks.name(textSerializerJson.copy().capabilities {
    requireCapability("${project.group}:adventure-text-serializer-json-benchmarks:${project.version}")
  })
  annotationProcessor(project(":adventure-annotation-processors"))
}

// Configure benchmarks to read from json project
plugins.withId("me.champeau.jmh") {
  configurations.named(JMHPlugin.JHM_RUNTIME_CLASSPATH_CONFIGURATION) {
    extendsFrom(sharedBenchmarks.get())
  }
  tasks.named("jmhRunBytecodeGenerator", JmhBytecodeGeneratorTask::class) {
    classesDirsToProcess.from(sharedBenchmarks.get())
  }
}
