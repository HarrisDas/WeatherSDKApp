pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://maven.pkg.github.com/HarrisDas/WeatherSDKApp/")
            credentials {
                username = System.getenv("GH_USERNAME")
                password = System.getenv("GH_PASSWORD")
            }
        }
    }
}

rootProject.name = "WeatherApp"
include(":app")
include(":WeatherSDK")
