package com.akyljer.di

import android.content.Context
import androidx.room.Room
import com.akyljer.data.local.AkylZherDatabase
import com.akyljer.data.local.dao.AlertDao
import com.akyljer.data.local.dao.AnimalCaseDao
import com.akyljer.data.local.dao.CropTaskDao
import com.akyljer.data.local.dao.FarmerProfileDao
import com.akyljer.data.local.dao.FieldDao
import com.akyljer.data.local.dao.PhotoDiagnosisDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Room database and DAOs
 * 
 * This module creates a singleton instance of the database
 * and provides all DAO instances for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides singleton instance of AkylZherDatabase
     * 
     * Uses fallbackToDestructiveMigration for MVP.
     * For production, implement proper migration strategy.
     */
    @Provides
    @Singleton
    fun provideAkylZherDatabase(
        @ApplicationContext context: Context
    ): AkylZherDatabase {
        return Room.databaseBuilder(
            context,
            AkylZherDatabase::class.java,
            AkylZherDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // MVP: acceptable data loss on schema changes
            .build()
    }
    
    /**
     * Provides FarmerProfileDao
     */
    @Provides
    @Singleton
    fun provideFarmerProfileDao(database: AkylZherDatabase): FarmerProfileDao {
        return database.farmerProfileDao()
    }
    
    /**
     * Provides FieldDao
     */
    @Provides
    @Singleton
    fun provideFieldDao(database: AkylZherDatabase): FieldDao {
        return database.fieldDao()
    }
    
    /**
     * Provides CropTaskDao
     */
    @Provides
    @Singleton
    fun provideCropTaskDao(database: AkylZherDatabase): CropTaskDao {
        return database.cropTaskDao()
    }
    
    /**
     * Provides AlertDao
     */
    @Provides
    @Singleton
    fun provideAlertDao(database: AkylZherDatabase): AlertDao {
        return database.alertDao()
    }
    
    /**
     * Provides AnimalCaseDao
     */
    @Provides
    @Singleton
    fun provideAnimalCaseDao(database: AkylZherDatabase): AnimalCaseDao {
        return database.animalCaseDao()
    }
    
    /**
     * Provides PhotoDiagnosisDao
     */
    @Provides
    @Singleton
    fun providePhotoDiagnosisDao(database: AkylZherDatabase): PhotoDiagnosisDao {
        return database.photoDiagnosisDao()
    }
}
