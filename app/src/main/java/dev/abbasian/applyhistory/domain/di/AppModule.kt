package dev.abbasian.applyhistory.domain.di

import androidx.room.Room
import dev.abbasian.applyhistory.db.local.database.AppDatabase
import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .createFromAsset("database/applyhistory.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().companyDao() }
    single { CompanyRepository(db = get<AppDatabase>()) }
    viewModel { CompanyViewModel(repository = get()) }
}
