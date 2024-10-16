package dev.abbasian.applyhistory.business.usecase.company

import dev.abbasian.applyhistory.R
import dev.abbasian.applyhistory.core.extension.UiText
import dev.abbasian.applyhistory.domain.model.DataResult

fun emptyFieldValidatorUseCase(input: String): DataResult {
    if (input.isEmpty()) {
        return DataResult(
            success = false,
            failure = UiText.StringResource(resId = R.string.strEmptyField)
        )
    }

    return DataResult(
        success = true
    )
}