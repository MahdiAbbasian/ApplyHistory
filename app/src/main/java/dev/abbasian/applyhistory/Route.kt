package dev.abbasian.applyhistory

sealed class Route(
    val route: String,
){
    object HomeScreen: Route("home_screen")
    object AddCompanyScreen: Route("add_company_screen")
}