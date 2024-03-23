package dev.abbasian.applyhistory.domain.repo

import dev.abbasian.applyhistory.db.local.AppDatabase
import dev.abbasian.applyhistory.domain.model.Company
import kotlinx.coroutines.flow.Flow

class CompanyRepository(private val db: AppDatabase) {

    fun getCompanies(): Flow<List<Company>> = db.companyDao().getAll()

    fun getCompaniesCount(): Flow<Int> = db.companyDao().getCompaniesCount()

    suspend fun getCompany(id: Int): Company? = db.companyDao().getCompany(id)

    suspend fun addCompany(company: Company) = db.companyDao().insert(company)

    suspend fun updateCompany(company: Company) = db.companyDao().update(company)

    suspend fun deleteCompany(id: Int) = db.companyDao().deleteCompany(id)
}