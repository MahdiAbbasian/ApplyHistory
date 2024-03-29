package dev.abbasian.applyhistory.db.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.abbasian.applyhistory.domain.model.CompanyEntity

@Database(entities = [CompanyEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
}