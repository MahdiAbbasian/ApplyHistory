package dev.abbasian.applyhistory

sealed class Route(
    val route: String,
){
    object Company: Route("company")
}