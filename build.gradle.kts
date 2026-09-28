plugins {
    id("java")
    id("base.java")
    id("configuration.shaded_dependencies")
}

dependencies {
    compileOnly(libs.viaversion.common)
    compileOnly(libs.viabackwards.common)
    compileOnly(libs.viarewind.common)
    compileOnly(libs.mcproxy)
    shadedDependencies(libs.reflect)
}

tasks {
    processResources {
        val projectVersion = project.version
        filesMatching("mcproxy.json") {
            expand(mapOf("version" to projectVersion))
        }
    }
}
