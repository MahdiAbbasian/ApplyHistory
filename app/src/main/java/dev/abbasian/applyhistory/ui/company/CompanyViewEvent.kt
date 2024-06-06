package dev.abbasian.applyhistory.ui.company

import android.content.Context
import android.net.Uri
import dev.abbasian.applyhistory.domain.model.CompanyEntity

sealed class CompanyViewEvent {
    object LoadCompanies : CompanyViewEvent()
    data class SearchCompany(val query: String) : CompanyViewEvent()
    data class ExportData(val context: Context, val onCompletion: (Boolean, String) -> Unit) : CompanyViewEvent()
    data class ImportData(val context: Context, val uri: Uri, val onCompletion: (Boolean) -> Unit) : CompanyViewEvent()
    data class GetCompanyDetail(val companyId: Int) : CompanyViewEvent()
    data class DeleteCompany(val companyId: Int) : CompanyViewEvent()
    data class AddCompany(val company: CompanyEntity) : CompanyViewEvent()
    data class UpdateCompany(
        val id: Int,
        val description: String?,
        val companyName: String?,
        val companyWebsite: String?,
        val lastUpdateDate: String?,
        val applyStatus: Int?
    ) : CompanyViewEvent()
}
