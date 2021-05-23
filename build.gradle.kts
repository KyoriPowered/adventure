plugins {
    idea
    val indraVersion = "2.0.4"
    id("net.kyori.indra") version indraVersion
    id("net.kyori.indra.publishing.sonatype") version indraVersion
    id("net.kyori.indra.license-header") version indraVersion
    id("net.kyori.indra.checkstyle") version indraVersion
    id("me.champeau.jmh") version "0.6.4"
}

val javacc: Configuration by configurations.creating

group = "net.kyori"
version = "4.1.0-SNAPSHOT"
description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

repositories {
    mavenCentral()
}

dependencies {
    api(libs.adventure.api)

    javacc(libs.javacc)

    testImplementation(libs.adventure.text.plain)
    testImplementation(libs.adventure.text.gson)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    checkstyle(libs.checkstyle)
}

indra {
    javaVersions {
        testWith(8, 11, 16)
    }

    github("KyoriPowered", "adventure-text-minimessage") {
        ci(true)
    }
    mitLicense()

    configurePublications {
        pom {
            developers {
                developer {
                    id.set("minidigger")
                    name.set("MiniDigger")
                }
            }
        }
    }
}

val parserSource = layout.buildDirectory.dir("gen-src-parser")

val generateParser by tasks.registering(JavaExec::class) {
    val parserName = "MiniParser"
    val genPackage = "net/kyori/adventure/text/minimessage/parser/gen"

    val src = layout.projectDirectory.file("src/main/grammars/${parserName}.jj")
    val dst = parserSource.map { it.dir(genPackage) }

    classpath = javacc
    main = "javacc"

    inputs.file(src)
        .withPropertyName("inputFile")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    outputs.dir(parserSource)
        .withPropertyName("outputDir")

    doFirst {
        val destination = dst.get().asFile
        destination.mkdirs()
        args(
            "-output_directory=${destination.absolutePath}",
            src.asFile.absolutePath
        )
    }
}

sourceSets {
    main {
        java {
            // JFlex output
            srcDir(generateParser)
        }
    }
}

idea {
    module {
        generatedSourceDirs.add(parserSource.get().asFile)
    }
}

license {
    exclude("**/gen/**")
}

tasks.checkstyleJmh {
    exclude("**")
}
tasks.checkstyleMain {
    exclude("**/gen/**")
}

tasks.checkstyleMain {
    exclude("**/gen/**")
}

tasks.jar {
    manifest.attributes(
        "Automatic-Module-Name" to "net.kyori.adventure.text.minimessage"
    )
}
