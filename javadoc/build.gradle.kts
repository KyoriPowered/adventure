plugins {
  id("adventure.aggregate-javadoc-conventions")
}

dependencies {
  listOf(
    "api",
    "key",
    "nbt",
    "serializer-configurate4",
    "text-logger-slf4j",
    "text-minimessage",
    "text-serializer-commons",
    "text-serializer-ansi",
    "text-serializer-gson",
    "text-serializer-json",
    "text-serializer-json-legacy-impl",
    "text-serializer-legacy",
    "text-serializer-plain"
  ).forEach {
    javadoc(project(":adventure-$it"))
  }

  javadocClasspath(libs.jspecify)
  javadocClasspath(libs.jetbrainsAnnotations)
  javadocClasspath(libs.autoService)
  javadocClasspath(libs.slf4j)
}

indra {
  configurePublications {
    // Publish only the aggregated docs jar, not a normal Java component.
    artifact(tasks.javadocJar)

    pom {
      name = "Adventure Javadoc"
      description = "Aggregated Javadocs for Adventure"
    }
  }
}

tasks.named<Javadoc>("javadoc").configure {
  title = "Adventure $version"
  options.overview = "src/main/javadoc/overview.html"
}
