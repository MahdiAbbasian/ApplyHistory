package dev.abbasian.applyhistory.business.usecase.company

import dev.abbasian.applyhistory.R
import dev.abbasian.applyhistory.core.extension.UiText
import dev.abbasian.applyhistory.core.extension.isProtocolValid
import dev.abbasian.applyhistory.core.extension.isWebsiteValid
import dev.abbasian.applyhistory.domain.model.DataResult

fun companyWebSiteValidatorUseCase(input: String): DataResult {
    if (!isProtocolValid(input)) {
        return DataResult(
            success = false,
            failure = UiText.StringResource(resId = R.string.strTheUrlMustStartWithHttpOrHttps)
        )
    }

    if (!isWebsiteValid(input)) {
        return DataResult(
            success = false,
            failure = UiText.StringResource(resId = R.string.strTheUrlIsNotValid)
        )
    }

    return DataResult(
        success = true
    )
}