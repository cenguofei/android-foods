package cn.example.foods.composefoods.navigation

enum class Screens(val route: String) {
    Home("home"),
    Login("login"),
    SellerDetail("seller"),
    Start("start"),
    MyOrder("start"),
    Favorite("favorite"),
    Search("search"),
    FoodDetail("food_detail")
}

object NavigationRoutes {
    const val LOGIN_NAVIGATION: String = "login_route"

    const val HOME_NAVIGATION = "home_route"
}
