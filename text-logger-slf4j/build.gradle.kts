plugins {
  id("adventure.common-conventions")
}

val exposedVersion by configurations.creating {
}

configurations {
  apiElements {
    extendsFrom(exposedVersion)
  }
  runtimeClasspath {
    extendsFrom(exposedVersion)
  }
  runtimeElements {
    extendsFrom(exposedVersion)
  }
}

dependencies {
  api(projects.adventureApi)
  compileOnly(libs.slf4j)
  exposedVersion(libs.slf4jRuntime)
  testImplementation(libs.slf4jtest)
  testImplementation(libs.slf4j)
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
