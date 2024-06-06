package dev.abbasian.applyhistory.ui.company.edit

import dev.abbasian.applyhistory.core.extension.UiText

data class EditCompanyViewState(
    val companyName: String = "",
    val companyWebsite: String = "",
    val description: String = "",
    val applyStatus: ApplyStatus = ApplyStatus.NONE,
    val isCompanyWebsiteValid: Boolean = true,
    val companyWebsiteError: UiText? = null,
    val isCompanyNameValid: Boolean = true,
    val companyNameError: UiText? = null
)