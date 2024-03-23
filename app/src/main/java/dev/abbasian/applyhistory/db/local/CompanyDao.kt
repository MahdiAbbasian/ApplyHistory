package dev.abbasian.applyhistory.db.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.abbasian.applyhistory.domain.model.Company
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
    @Query("SELECT * FROM Company ORDER BY id DESC")
    fun getAll(): Flow<List<Company>>

    @Query("SELECT * FROM Company WHERE id = :id")
    suspend fun getCompany(id: Int): Company?

    @Query("SELECT COUNT(id) FROM Company")
    fun getCompaniesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg companies: Company)

    @Update
    suspend fun update(company: Company)

    @Query("DELETE FROM Company WHERE id = :id")
    suspend fun deleteCompany(id: Int)
}