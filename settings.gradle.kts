pluginManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.google.com")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.google.com")
        }
        mavenCentral()
    }
}

rootProject.name = "ApplyHistory"
include(":app")
