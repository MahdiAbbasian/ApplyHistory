package dev.abbasian.applyhistory.ui.company

import dev.abbasian.applyhistory.domain.model.CompanyEntity

sealed class CompanyViewState {
    object Loading : CompanyViewState()
    data class Success(val companies: List<CompanyEntity>) : CompanyViewState()
    data class Error(val message: String) : CompanyViewState()
}
