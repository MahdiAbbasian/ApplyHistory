package dev.abbasian.applyhistory

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String) {
    companion object {
        val HomeScreen = "home_screen"
        val AddCompanyScreen = "add_edit_company_screen"
        fun editCompanyScreen(companyId: Int?) = "add_edit_company_screen/${companyId ?: "{companyId}"}"
        fun companyDetailScreen(companyId: Int?) = "companyDetail/${companyId ?: "{companyId}"}"
    }

    object Home : Route(HomeScreen)
    object AddCompany : Route(AddCompanyScreen)
    data class EditCompany(val companyId: Int? = null) : Route(editCompanyScreen(companyId))
    data class CompanyDetail(val companyId: Int? = null) : Route(companyDetailScreen(companyId))
}