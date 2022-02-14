plugins {
  id("adventure.common-conventions")
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.forUseAtConfigurationTime().get())
  }
}

dependencies {
  api("net.kyori:examination-api:1.3.0")
  api("net.kyori:examination-string:1.3.0")
  compileOnlyApi("org.jetbrains:annotations:23.0.0")
  testImplementation("com.google.guava:guava:31.0.1-jre")
}

applyJarMetadata("net.kyori.adventure.key")
