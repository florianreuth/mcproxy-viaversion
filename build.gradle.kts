import de.florianreuth.baseproject.configureShadedDependencies
import de.florianreuth.baseproject.setupProject

plugins {
    id("java")
    id("de.florianreuth.baseproject")
}

setupProject()

repositories {
    maven("https://repo.viaversion.com")
    maven("https://jitpack.io/")
}

val shade = configureShadedDependencies()

dependencies {
    compileOnly("com.viaversion:viaversion-common:5.8.0")
    compileOnly("com.viaversion:viabackwards-common:5.8.0")
    compileOnly("com.viaversion:viarewind-common:4.0.15")
    compileOnly("com.github.Outfluencer:mcproxy:d08d6a3eee")
    shade("net.lenni0451:Reflect:1.6.2")
}

tasks {
    processResources {
        val projectVersion = project.version
        filesMatching("mcproxy.json") {
            expand(mapOf("version" to projectVersion))
        }
    }
}
