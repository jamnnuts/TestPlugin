import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("plugin.serialization") version "1.9.22"
    id("idea")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("java-library")
}

group = "com.kalentire.test"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://repo.onarandombox.com/content/groups/public/")

    maven("https://jitpack.io")
}

val exposedVersion = "0.40.1"

dependencies {
    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")

    library("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-api", "2.16.0")
    library("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-core", "2.16.0")
    library("com.google.guava:guava:18.0")

    library("org.joml:joml:1.10.5")
    //library("org.jetbrains.exposed:exposed-core:$exposedVersion")
    //library("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    //library("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    //library("org.xerial:sqlite-jdbc:3.30.1")
    //library("com.impossibl.pgjdbc-ng", "pgjdbc-ng", "0.8.3")
    //library("mysql:mysql-connector-java:8.0.30")
    //library("com.zaxxer:HikariCP:4.0.3")

    library("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation("com.github.RAVINGAR.RavinAPI:common:1.5.8")
    implementation("com.github.RAVINGAR.RavinAPI:module:1.5.8")
    implementation("com.github.RAVINGAR.RavinAPI:module-kotlin:1.5.8")
    implementation("com.github.RAVINGAR.RavinAPI:gui:1.5.8")

    compileOnly("org.jetbrains:annotations:23.1.0")

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    compileOnly("net.kyori:adventure-api:4.13.1")
}

tasks {
    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    jar {
        archiveFileName.set("HeroBar-parent.jar")
        archiveBaseName.set("HeroBar-parent")
        archiveVersion.set(null as String?)
        archiveClassifier.set("original")
    }

    shadowJar {
        archiveFileName.set("HeroBar.jar")
        archiveBaseName.set("HeroBar")
        archiveVersion.set(null as String?)
        archiveClassifier.set("")
        relocate("com.ravingarinc.api", "com.herocraftonline.herobar.libs.api")
    }


    artifacts {
        archives(shadowJar)
    }

    register<Copy>("copyToDev") {
        from(shadowJar)
        into(project.layout.projectDirectory.dir("../../Desktop/Programming/Servers/Latest/plugins"))
        //into "E:/Documents/Workspace/Servers/1.18.2-TEST/plugins/"
    }

    assemble {
        dependsOn(shadowJar)
        finalizedBy("copyToDev")
    }
    test {
        useJUnitPlatform()
        // Ensure testing is never "up-to-date" (in Gradle-speak), which means it can never be skipped,
        // as it would otherwise be.
        outputs.upToDateWhen { false }

        // Ensure we get all the useful test output.
        testLogging {
            events("failed", "passed", "skipped")
            showExceptions = true
            showCauses = true
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

bukkit {

    name = "TestPlugin"
    version = project.version as String
    description = "Put the description here"
    main = "com.kalentire.test.TestPlugin"

    // API version (should be set for 1.13+)
    apiVersion = "1.20"

    // Other possible properties from plugin.yml (optional)
    author = "RAVINGAR"

    commands {
        register("skills") {
            permission = "herobar.skills"
            description = "Open the skill menu!"
            usage = "Unknown argument. Try /herobar:skills"
        }
    }
}
