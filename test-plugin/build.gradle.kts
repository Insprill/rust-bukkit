plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.insprill"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot-API
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }
    compileJava {
        options.compilerArgs.addAll(listOf("-h", "${file("${buildDir}/native")}"))
    }
    shadowJar {
        archiveClassifier.set("")
        from(buildRustLib())
    }
    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

fun buildRustLib(): List<File> {
    val rustDir = file("rust")
    exec {
        commandLine("cargo", "build", "--release", "--manifest-path=$rustDir${File.separator}Cargo.toml")
    }
    val releaseDir = File(rustDir.absolutePath, "target${File.separator}release")
    val files = ArrayList<File>()
    for (file in releaseDir.listFiles()!!) {
        when (file.name) {
            "test_plugin.dll" -> files.add(file)
            "test_plugin.so" -> files.add(file)
            "test_plugin.dylib" -> files.add(file)
        }
    }
    if (files.isEmpty()) {
        throw IllegalStateException("Failed to find any native libraries")
    }
    return files
}
