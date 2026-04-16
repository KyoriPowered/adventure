plugins {
  id("adventure.base-conventions")
  id("io.freefair.aggregate-javadoc")
}

tasks.named<Javadoc>("javadoc").configure {
  (options as StandardJavadocDocletOptions).applyCommonJavadocTags()
}

indra {
  configurePublications {
    artifact(tasks.javadocJar)
  }
}
