pluginManagement {
    includeBuild("build-logic")

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

rootProject.name = "foodsapp"
include(":app")
include(":core:network")
include(":core:designsystem")
include(":core:database")
include(":feature:settings")
include(":core:model")
include(":core:datastore")
include(":core:common")
include(":feature:start")
include(":feature:home")
include(":feature:lwh")
include(":feature:ysq")
include(":feature:login")
include(":feature:search")
include(":feature:sellerdetail")
