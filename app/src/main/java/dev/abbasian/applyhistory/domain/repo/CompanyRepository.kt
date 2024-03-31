package dev.abbasian.applyhistory.domain.repo

import androidx.lifecycle.asFlow
import dev.abbasian.applyhistory.db.local.database.AppDatabase
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CompanyRepository(private val db: AppDatabase) {

    fun getCompanies(): Flow<List<CompanyEntity>> = db.companyDao().getAll().asFlow()

    fun getCompaniesCount(): Flow<Int> = db.companyDao().getCompaniesCount().asFlow()

    suspend fun getCompany(id: Int): CompanyEntity? = withContext(Dispatchers.IO) {
        db.companyDao().getCompany(id)
    }

    suspend fun addCompany(company: CompanyEntity) = withContext(Dispatchers.IO) {
        db.companyDao().insert(company)
    }

    suspend fun updateCompany(id: Int, description: String?, companyName: String?, companyWeb: String?, lastUpdateDate: String?, applyStatus: Int?) = withContext(Dispatchers.IO) {
        db.companyDao().update(description, companyName, companyWeb, lastUpdateDate, applyStatus, id)
    }

    suspend fun deleteCompany(id: Int) = withContext(Dispatchers.IO) {
        db.companyDao().deleteCompany(id)
    }
}