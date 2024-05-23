package dev.abbasian.applyhistory.business.usecase

interface BaseUseCase<Input, Output> {
    fun perform(from: Input): Output
}