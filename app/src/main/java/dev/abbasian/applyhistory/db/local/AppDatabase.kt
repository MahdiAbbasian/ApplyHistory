package dev.abbasian.applyhistory.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.abbasian.applyhistory.domain.model.Company

@Database(
    entities = [Company::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao

    companion object {
        const val DATABASE_NAME = "myDB"
    }
}