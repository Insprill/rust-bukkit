import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.rikonardo.papermake") version "1.0.4"
}

group = "net.insprill"
version = getFullVersion()

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot-API
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.compilerArgs.addAll(listOf("-h", "${file("${buildDir}/native")}"))
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }
    shadowJar {
        archiveClassifier.set("")
        from("LICENSE")
        from(buildRustLib())
    }
    build {
        dependsOn(shadowJar)
    }
}


fun buildRustLib(): File? {
    val rustDir = file("rust")
    exec {
        commandLine("cargo", "build", "--release", "--manifest-path=$rustDir/Cargo.toml")
    }
    val releaseDir = File(rustDir.absolutePath, "target/release")
    for (file in releaseDir.listFiles()!!) {
        when (file.name) {
            "rust_bukkit.dll" -> return file
            "rust_bukkit.so" -> return file
            "rust_bukkit.dylib" -> return file
        }
    }
    return null
}

fun getFullVersion(): String {
    val version = project.property("version")!! as String
    return if (version.contains("-SNAPSHOT")) {
        "$version+rev.${getGitHash()}"
    } else {
        version
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
