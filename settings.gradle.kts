pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
includeBuild("lib/hotwire-native-android") {
    dependencySubstitution {
        substitute(module("dev.hotwire:core")).using(project(":core"))
        substitute(module("dev.hotwire:navigation-fragments")).using(project(":navigation-fragments"))
    }
}

rootProject.name = "BlackCandy"
include(":androidApp")
include(":shared")
