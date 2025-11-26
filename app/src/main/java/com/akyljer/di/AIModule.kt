package com.akyljer.di

import android.content.Context
import com.akyljer.ai.DiseaseInfoMapper
import com.akyljer.ai.PlantDiseaseDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for AI/ML components
 * 
 * Provides TensorFlow Lite model and related AI services
 */
@Module
@InstallIn(SingletonComponent::class)
object AIModule {
    
    @Provides
    @Singleton
    fun providePlantDiseaseDetector(
        @ApplicationContext context: Context
    ): PlantDiseaseDetector {
        return PlantDiseaseDetector(context)
    }
    
    @Provides
    @Singleton
    fun provideDiseaseInfoMapper(): DiseaseInfoMapper {
        return DiseaseInfoMapper()
    }
}
