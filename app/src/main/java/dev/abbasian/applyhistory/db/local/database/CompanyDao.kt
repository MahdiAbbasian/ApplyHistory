package dev.abbasian.applyhistory.db.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
        @Query("SELECT * FROM CompanyEntity ORDER BY id DESC")
        fun getAll(): LiveData<List<CompanyEntity>>

        @Query("SELECT * FROM CompanyEntity WHERE id = :id")
        fun getCompany(id : Int): CompanyEntity?

        @Query("SELECT COUNT(id) FROM CompanyEntity")
        fun getCompaniesCount(): LiveData<Int>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(vararg questions: CompanyEntity)

        @Query("UPDATE CompanyEntity SET description = :description, companyName= :companyName, companyWebSite= :companyWeb, lastUpdateDate= :lastUpdateDate, applyStatus= :applyStatus WHERE id =:id")
        suspend fun update(description: String?, companyName: String?,companyWeb: String?,lastUpdateDate: String?, applyStatus: Int?, id: Int)

        @Query("DELETE FROM CompanyEntity WHERE id=(:id)")
        fun deleteCompany(id:Int)
}