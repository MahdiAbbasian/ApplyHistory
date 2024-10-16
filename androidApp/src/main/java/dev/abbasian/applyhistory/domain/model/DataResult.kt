package dev.abbasian.applyhistory.domain.model

import dev.abbasian.applyhistory.core.extension.UiText

data class DataResult(
    val success: Boolean,
    val failure: UiText? = null
)