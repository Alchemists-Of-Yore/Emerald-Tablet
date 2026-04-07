plugins {
    // https://projects.neoforged.net/neoforged/ModDevGradle
    id("net.neoforged.moddev") version "2.0.141"
    idea
}

version = mod["version"]
group = mod["group"]
base.archivesName = mod["id"]

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = neo["version"]

    validateAccessTransformers = true
    interfaceInjectionData.from(files("src/main/resources/META-INF/interfaceinjections.json"))

    parchment {
        mappingsVersion = project.parchment["mappingsVersion"]
        minecraftVersion = project.parchment["minecraftVersion"]
    }

    runs {
        create("client") {
            client()
            devLogin = true
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
        }

        create("server") {
            server()
            programArgument("--nogui")
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod", mod["id"],
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }
    }

    mods {
        create(mod["id"]) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

val localRuntime: Configuration by configurations.creating

configurations {
    runtimeClasspath {
        extendsFrom(localRuntime)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven {
                name = "CurseMaven"
                url = uri("https://cursemaven.com")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Registrate"
                url = uri("https://mvn.devos.one/snapshots")
            }
        }
        filter {
            includeGroup("com.tterrag.registrate")
        }
    }
    maven { url = uri("https://maven.blamejared.com/") }
    maven { url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") }
    maven { url = uri("https://maven.terraformersmc.com/") }
    flatDir { dirs("libs") }
}

dependencies {
    compileOnly("mezz.jei:jei-${mc["version"]}-common-api:${deps["jei"]}")
    compileOnly("mezz.jei:jei-${mc["version"]}-neoforge-api:${deps["jei"]}")
    localRuntime("mezz.jei:jei-${mc["version"]}-neoforge:${deps["jei"]}")

    localRuntime("maven.modrinth:sodium:mc1.21.1-0.6.13-neoforge")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to mc["version"],
        "minecraft_version_range" to mc["versionRange"],
        "neo_version" to neo["version"],
        "neo_version_range" to neo["versionRange"],
        "loader_version_range" to neo["loaderVersionRange"],
        "mod_id" to mod["id"],
        "mod_name" to mod["name"],
        "mod_license" to mod["license"],
        "mod_version" to mod["version"],
        "mod_authors" to mod["authors"],
        "mod_description" to mod["description"]
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
