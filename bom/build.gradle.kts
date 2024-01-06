plugins {
  id("java-platform")
  id("adventure.base-conventions")
}

indra {
  configurePublications {
    from(components["javaPlatform"])
  }
}

dependencies {
  constraints {
    sequenceOf(
      "api",
      "annotation-processors",
      "extra-kotlin",
      "key",
      "nbt",
      "serializer-configurate4",
      "text-logger-slf4j",
      "text-minimessage",
      "text-serializer-ansi",
      "text-serializer-gson",
      "text-serializer-json",
      "text-serializer-json-legacy-impl",
      "text-serializer-legacy",
      "text-serializer-plain"
    ).forEach {
      api(project(":adventure-$it"))
    }
  }
}
