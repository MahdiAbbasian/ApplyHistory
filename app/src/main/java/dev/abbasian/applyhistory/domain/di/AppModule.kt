package dev.abbasian.applyhistory.domain.di

import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.db.local.AppDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(get()) }
    single { CompanyRepository(get()) }
    viewModel { CompanyViewModel(get()) }
}