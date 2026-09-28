pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("base.settings")
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.viaversion.com")
        maven("https://libraries.minecraft.net")
        maven("https://jitpack.io/")
    }
}

rootProject.name = "mcproxy-viaversion"
