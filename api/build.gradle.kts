plugins {
  id("adventure.common-conventions")
  id("me.champeau.jmh")
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.forUseAtConfigurationTime().get())
  }
}

dependencies {
  api(project(":adventure-key"))
  api("net.kyori:examination-api:1.3.0")
  api("net.kyori:examination-string:1.3.0")
  compileOnlyApi("org.jetbrains:annotations:22.0.0")
  testImplementation("com.google.guava:guava:23.0")
}

applyJarMetadata("net.kyori.adventure")
