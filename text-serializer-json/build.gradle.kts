plugins {
  id("adventure.common-conventions")
  `java-test-fixtures`
  alias(libs.plugins.jmh) apply false
}

val jmh by sourceSets.registering {
  compileClasspath += sourceSets.main.get().compileClasspath
  runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

val jmhJar by tasks.registering(Jar::class) {
  archiveClassifier.set("jmh")
  from(jmh.map { it.output })
}

configurations.register("jmh") {
  attributes {
    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
  }


  outgoing {
    capabilities.clear()
    capability("${project.group}:${project.name}-benchmarks:${project.version}")

    artifact(jmhJar.flatMap { it.archiveFile })
    variants.create("classes") {
      attributes {
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
      }
      artifact(jmh.map { it.output.classesDirs.singleFile }) {
        type = "directory"
      }
    }
  }
}

dependencies {
  api(projects.adventureApi)
  annotationProcessor(projects.adventureAnnotationProcessors)
  testFixturesApi(libs.junit.api)
  testFixturesApi(platform(libs.junit.bom))

  "jmhImplementation"(sourceSets.main.map { it.output })
  "jmhImplementation"("${me.champeau.jmh.JMHPlugin.JMH_CORE_DEPENDENCY}${libs.versions.jmh.get()}")
  "jmhImplementation"("${me.champeau.jmh.JMHPlugin.JMH_GENERATOR_DEPENDENCY}${libs.versions.jmh.get()}")
}

applyJarMetadata("net.kyori.adventure.text.serializer.json")

// Don't publish test fixtures for external consumers
(components["java"] as AdhocComponentWithVariants).run {
  withVariantsFromConfiguration(configurations.testFixturesApiElements.get()) { skip() }
  withVariantsFromConfiguration(configurations.testFixturesRuntimeElements.get()) { skip() }
}
