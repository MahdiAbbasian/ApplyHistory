pluginManagement {
    repositories {
        google()
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
       google()
        maven {
            url = uri("https://maven.google.com")
        }
        mavenCentral()
    }
//    versionCatalogs {
//        libs {
//            from(files("gradle/libs.versions.toml"))
//        }
//    }
}

rootProject.name = "ApplyHistory"
include(":app")
include(":shared")