/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.slimjar)
}

group = "ruben_artz"
version = "5.5.21"

registerOutputTask("Ruben_Artz", "F:/Ruben_Artz/Artz Studio/1.21.11/plugins")

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.onarandombox.com/content/groups/public/")
    maven("https://repo.glaremasters.me/repository/concuncan/")
    maven("https://repository.rubenmatias.com/releases")
    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.spigotmc)

    compileOnly(libs.lombok)
    compileOnly(libs.authlib)
    compileOnly(libs.slimeworldmanager)
    compileOnly(libs.multiversecore)
    compileOnly(libs.placeholderapi)

    compileOnly(fileTree(mapOf("dir" to "libs", "includes" to listOf("*.jar"))))
    annotationProcessor(libs.lombok)

    implementation(libs.slimjarRuntime)
    implementation(libs.slimjarHelperSpigot)
    implementation(libs.minimessage)
    implementation(libs.adventurePlatformBukkit)
    implementation(libs.bstats)

    slim(libs.boostedYaml)
    slim(libs.universalScheduler)
    slim(libs.h2)
    slim(libs.hikarycp)
    slim(libs.xseries)
    slim(libs.okhttps)
    slim(libs.gson)
}

tasks {
    shadowJar {
        archiveFileName.set("Deluxe Void World.jar")

        relocate("io.github.slimjar", "${libs.versions.libsBase.get()}slimjar")
        relocate("net.kyori", "${libs.versions.libsBase.get()}kyori")
        relocate("org.bstats", "${libs.versions.libsBase.get()}bstats")
        relocate("io.papermc.lib", "${libs.versions.libsBase.get()}paperlib")
    }

    slimJar {
        relocate("com.cryptomorin.xseries", "${libs.versions.libsBase.get()}xseries")
        relocate("com.zaxxer.hikari", "${libs.versions.libsBase.get()}hikari")
        relocate("org.h2", "${libs.versions.libsBase.get()}h2")
        relocate("okhttp3", "${libs.versions.libsBase.get()}okhttp3")
        relocate("com.github.Anon8281.universalScheduler", "${libs.versions.libsBase.get()}universalScheduler")
        relocate("dev.dejvokep.boostedyaml", "${libs.versions.libsBase.get()}boostedyaml")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

fun registerOutputTask(name: String, path: String) {
    if (!System.getProperty("os.name").lowercase().contains("windows")) {
        return
    }

    tasks.register<Copy>("build$name") {
        group = "build plugin"
        dependsOn(tasks.shadowJar)
        from(tasks.shadowJar.get().archiveFile)
        into(file(path))
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}