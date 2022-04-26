plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(libs.slf4j)
  implementation(libs.slf4j.ext) {
    exclude(group = "org.slf4j", module = "slf4j-api")
  }
  testImplementation(libs.slf4j.simple)
}

sourceSets.main {
  multirelease {
    alternateVersions(9)
  }
}
