package com.akyljer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akyljer.data.local.dao.AlertDao
import com.akyljer.data.local.dao.AnimalCaseDao
import com.akyljer.data.local.dao.CropTaskDao
import com.akyljer.data.local.dao.FarmerProfileDao
import com.akyljer.data.local.dao.FieldDao
import com.akyljer.data.local.dao.PhotoDiagnosisDao
import com.akyljer.data.local.entity.AlertEntity
import com.akyljer.data.local.entity.AnimalCaseEntity
import com.akyljer.data.local.entity.CropTaskEntity
import com.akyljer.data.local.entity.FarmerProfileEntity
import com.akyljer.data.local.entity.FieldEntity
import com.akyljer.data.local.entity.PhotoDiagnosisEntity

/**
 * Room Database for Акыл Жер (AkylZher) MVP
 * 
 * This is the main database for the app, providing offline-first storage.
 * 
 * Entities:
 * - FarmerProfileEntity: Farmer personal info
 * - FieldEntity: Field/land information
 * - CropTaskEntity: AI-generated or manual tasks
 * - AlertEntity: Alerts from various sources
 * - AnimalCaseEntity: Animal health cases (AgroVet)
 * - PhotoDiagnosisEntity: Photo Doctor diagnosis history
 * 
 * Version: 1 (initial MVP version)
 * 
 * Migration Strategy:
 * For MVP, we use fallbackToDestructiveMigration (data loss on schema change is acceptable).
 * For production, implement proper migrations.
 */
@Database(
    entities = [
        FarmerProfileEntity::class,
        FieldEntity::class,
        CropTaskEntity::class,
        AlertEntity::class,
        AnimalCaseEntity::class,
        PhotoDiagnosisEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AkylZherDatabase : RoomDatabase() {
    
    // DAO accessors
    abstract fun farmerProfileDao(): FarmerProfileDao
    abstract fun fieldDao(): FieldDao
    abstract fun cropTaskDao(): CropTaskDao
    abstract fun alertDao(): AlertDao
    abstract fun animalCaseDao(): AnimalCaseDao
    abstract fun photoDiagnosisDao(): PhotoDiagnosisDao
    
    companion object {
        const val DATABASE_NAME = "akylzher_database"
    }
}
