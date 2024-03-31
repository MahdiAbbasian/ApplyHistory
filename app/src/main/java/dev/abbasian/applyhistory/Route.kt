package dev.abbasian.applyhistory

sealed class Route(
    val route: String,
){
    object HomeScreen: Route("home_screen")
    object AddCompanyScreen : Route("add_edit_company_screen")

    object EditCompanyScreen : Route("add_edit_company_screen/{companyId}") {
        fun createRoute(companyId: Int): String = "add_edit_company_screen/$companyId"
    }

    object CompanyDetailScreen : Route("companyDetail/{companyId}") {
        fun createRoute(companyId: Int) = "companyDetail/$companyId"
    }
}