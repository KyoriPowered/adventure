import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
  target {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_11
    }
  }
}
