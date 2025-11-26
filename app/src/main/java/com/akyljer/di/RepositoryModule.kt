package com.akyljer.di

import com.akyljer.data.repository.AlertRepository
import com.akyljer.data.repository.AgroVetRepository
import com.akyljer.data.repository.CropAdvisorRepository
import com.akyljer.data.repository.FarmerRepository
import com.akyljer.data.repository.FieldRepository
import com.akyljer.data.repository.PhotoDoctorRepository
import com.akyljer.data.repository.impl.AlertRepositoryImpl
import com.akyljer.data.repository.impl.AgroVetRepositoryImpl
import com.akyljer.data.repository.impl.CropAdvisorRepositoryImpl
import com.akyljer.data.repository.impl.FarmerRepositoryImpl
import com.akyljer.data.repository.impl.FieldRepositoryImpl
import com.akyljer.data.repository.impl.PhotoDoctorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Repository implementations
 * 
 * Binds repository interfaces to their implementations
 * for dependency injection throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindFarmerRepository(
        impl: FarmerRepositoryImpl
    ): FarmerRepository
    
    @Binds
    @Singleton
    abstract fun bindFieldRepository(
        impl: FieldRepositoryImpl
    ): FieldRepository
    
    @Binds
    @Singleton
    abstract fun bindAlertRepository(
        impl: AlertRepositoryImpl
    ): AlertRepository
    
    @Binds
    @Singleton
    abstract fun bindCropAdvisorRepository(
        impl: CropAdvisorRepositoryImpl
    ): CropAdvisorRepository
    
    @Binds
    @Singleton
    abstract fun bindAgroVetRepository(
        impl: AgroVetRepositoryImpl
    ): AgroVetRepository
    
    @Binds
    @Singleton
    abstract fun bindPhotoDoctorRepository(
        impl: PhotoDoctorRepositoryImpl
    ): PhotoDoctorRepository
}
