plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(libs.slf4j)
  testImplementation(libs.slf4jtest)
}

sourceSets.main {
  multirelease {
    alternateVersions(9)
  }
}

applyJarMetadata("net.kyori.adventure.text.logger.slf4j")

eclipse {
  // Make sure slf4j doesn't end up on the module path until we are actually a module
  classpath.file.whenMerged {
    (this as org.gradle.plugins.ide.eclipse.model.Classpath).entries.forEach { entry ->
      if (entry is org.gradle.plugins.ide.eclipse.model.Library) {
        entry.entryAttributes["module"] = false
      }
    }
  }
}
