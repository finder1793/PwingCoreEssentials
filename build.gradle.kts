plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.7.10" // Update to a more recent version
}

group = "com.yourname"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.sk89q.com/repo/")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.sk89q.worldedit:worldedit-bukkit:7.2.0")
    implementation("com.sk89q.worldguard:worldguard-bukkit:7.0.4")
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT") // Switch to Spigot
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}
