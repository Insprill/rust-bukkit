plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.rikonardo.papermake") version "1.0.4"
}

group = "net.insprill"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot-API
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    implementation(project(":"))
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

fun buildRustLib(): File {
    val rustDir = file("rust")
    exec {
        commandLine("cargo", "build", "--release", "--manifest-path=$rustDir${File.separator}Cargo.toml")
    }
    val releaseDir = File(rustDir.absolutePath, "target${File.separator}release")
    val lib = File(releaseDir, System.mapLibraryName("test_plugin"))
    if (!lib.isFile) {
        throw IllegalStateException("Failed to find native library")
    }
    return lib
}
