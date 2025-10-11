plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.github.slimjar") version "1.3.0"
}

group = "ruben_artz"
version = "5.4.21"

val libsBase = "ruben_artz.world.libraries."

registerOutputTask("Ruben_Artz", "C:/Users/Ruben/Desktop/")

repositories {
    mavenCentral()
    maven {
        name = ("spigotmc-repo")
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = ("sonatype")
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = ("papermc")
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://repo.onarandombox.com/content/groups/public/")
    }
    maven {
        url = uri("https://repo.glaremasters.me/repository/concuncan/")
    }
    maven {
        name = "rubenmatiasReleases"
        url = uri("https://repo.stn-studios.dev/releases")
    }
    maven {
        url = uri("https://jitpack.io")
    }

    maven { url = uri("https://libraries.minecraft.net") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.42")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.grinderwolf:slimeworldmanager-api:2.2.1")
    compileOnly("com.onarandombox.multiversecore:Multiverse-Core:4.3.1")
    /*
    Keep up to date
    Url: https://www.spigotmc.org/resources/6245/
     */
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("org.jetbrains:annotations:23.0.0")

    compileOnly(fileTree(mapOf("dir" to "libs", "includes" to listOf("*.jar"))))
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("io.github.slimjar:slimjar:1.0.0")
    implementation("net.kyori:adventure-text-minimessage:4.25.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("io.papermc:paperlib:1.0.7")

    /*
    Keep up to date
    Url: https://github.com/Anon8281/UniversalScheduler
     */
    slim("com.github.Anon8281:UniversalScheduler:0.1.6")
    slim("com.zaxxer:HikariCP:4.0.3")
    slim("com.h2database:h2:2.1.214")
    slim("org.slf4j:slf4j-simple:2.0.3")
    /*
    Keep up to date
    Url: https://github.com/CryptoMorin/XSeries/releases
     */
    slim("com.github.cryptomorin:XSeries:13.5.1")
    slim("com.squareup.okhttp3:okhttp:4.12.0")
    slim("junit:junit:4.13.1")
    slim("com.google.code.gson:gson:2.13.1")
}

tasks.shadowJar {
    archiveFileName.set("Deluxe Void World.jar")

    relocate("io.github.slimjar", "${libsBase}slimjar")
    relocate("net.kyori", "${libsBase}kyori")
    relocate("org.bstats", "${libsBase}bstats")
    relocate("io.papermc.lib", "${libsBase}paperlib")
}

tasks.slimJar {
    relocate("com.cryptomorin.xseries", "${libsBase}xseries")
    relocate("com.zaxxer.hikari", "${libsBase}hikari")
    relocate("org.h2", "${libsBase}h2")
    relocate("okhttp3", "${libsBase}okhttp3")
    relocate("com.github.Anon8281.universalScheduler", "${libsBase}universalScheduler")
    relocate("com.tcoded.folialib", "${libsBase}folialib")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
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