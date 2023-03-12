plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(libs.build.errorpronePlugin)
  implementation(libs.build.indra)
  implementation(libs.build.indra.crossdoc)
  implementation(libs.build.indra.sonatype)
  implementation(libs.build.indra.spotless)
  implementation(libs.build.testLogger)
  compileOnly(libs.build.jmh)
  implementation(libs.build.goomph)
}

dependencies {
  compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
  target {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }
}
