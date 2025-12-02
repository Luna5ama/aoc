group = "dev.luna5ama"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.1.0"
    id("dev.luna5ama.jar-optimizer") version "1.2.2"
}

apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("it.unimi.dsi:fastutil:8.5.12")
}

tasks {
    val fatJar by creating(Jar::class) {
        group = "build"

        from(jar.get().archiveFile.map { zipTree(it) })
        from(configurations.runtimeClasspath.get().elements.map { set ->
            set.map {
                if (it.asFile.isDirectory) it else zipTree(
                    it
                )
            }
        })

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        archiveClassifier.set("fatjar")
    }

    val optimizeFatJar = jarOptimizer.register(
        fatJar,
        "dev.luna5ama.aoc.aoc24"
    )

    artifacts {
        archives(optimizeFatJar)
    }
}