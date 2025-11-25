package com.akyljer.core.cv

import android.content.Context
import android.graphics.Bitmap
import com.akyljer.core.model.Diagnosis
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoDoctorClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun analyze(bitmap: Bitmap): Diagnosis {
        // TODO: wire TensorFlow Lite model once available; stub returns healthy.
        return Diagnosis(
            label = "Healthy / Unknown",
            confidence = 0.4f,
            recommendation = "No clear disease detected. Continue monitoring."
        )
    }
}
