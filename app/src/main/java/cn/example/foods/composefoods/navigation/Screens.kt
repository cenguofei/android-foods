package cn.example.foods.composefoods.navigation

enum class Screens(val route: String) {
    HOME("home"),
    LOGIN("login")
}

object NavigationRoutes {
    const val LOGIN_NAVIGATION: String = "login_route"

    const val HOME_NAVIGATION = "home_route"
}
