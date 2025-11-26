package com.akyljer

import android.app.Application
import com.akyljer.data.seeding.DataSeedingManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Application class for Акыл Жер
 * 
 * Initializes Hilt and seeds initial data on first launch.
 */
@HiltAndroidApp
class AkylJerApp : Application() {
    
    @Inject
    lateinit var dataSeedingManager: DataSeedingManager
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        
        // Seed initial data if needed (first launch)
        applicationScope.launch {
            try {
                dataSeedingManager.seedIfNeeded()
            } catch (e: Exception) {
                // Log error but don't crash the app
                android.util.Log.e("AkylJerApp", "Error seeding data", e)
            }
        }
    }
}
