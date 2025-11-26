# Step 6 Quick Reference - AI/ML Repository Integration

## 📋 Overview
Extended Step 6 with **3 new AI/ML-enabled repositories** for Smart Farming features.

---

## 🗂️ Repository Summary

| Repository | Purpose | Status | ML Ready |
|------------|---------|--------|----------|
| FarmerRepository | Profile management | ✅ Done | N/A |
| FieldRepository | Field data | ✅ Done | N/A |
| AlertRepository | Alert aggregation | ✅ Done | N/A |
| **CropAdvisorRepository** | **AI crop recommendations** | ✅ NEW | 🔄 Mocked |
| **AgroVetRepository** | **Livestock health triage** | ✅ NEW | 🔄 Mocked |
| **PhotoDoctorRepository** | **Plant disease detection** | ✅ NEW | 🔄 Mocked |

---

## 🌾 CropAdvisorRepository

### Key Functions
```kotlin
// Generate AI-powered tasks
cropAdvisorRepository.generateCropTasks(fieldId, weatherData)

// Generate risk alerts
cropAdvisorRepository.generateRiskAlerts(fieldId, weatherData)

// Get crop recommendations
cropAdvisorRepository.getCropRecommendations(fieldId, season)

// Analyze field health
cropAdvisorRepository.analyzeFieldHealth(fieldId)

// Get irrigation schedule
cropAdvisorRepository.getIrrigationSchedule(fieldId, weatherData)
```

### Current Implementation
- ✅ Rule-based task generation (irrigation, fertilization, pest control, weeding)
- ✅ Weather-based alerts (frost, drought, heavy rain, wind)
- ✅ Soil-type based crop recommendations
- ✅ Simple health scoring

### TODO: ML Integration
- 🔲 Weather API integration (OpenWeather)
- 🔲 TFLite crop yield prediction model
- 🔲 Pest/disease risk forecasting
- 🔲 IoT sensor data integration
- 🔲 Satellite imagery (NDVI) analysis

### Usage Example
```kotlin
@HiltViewModel
class SmartFarmingViewModel @Inject constructor(
    private val cropAdvisorRepository: CropAdvisorRepository
) : ViewModel() {
    
    fun generateRecommendations(fieldId: String) {
        viewModelScope.launch {
            val recommendations = cropAdvisorRepository.getCropRecommendations(
                fieldId = fieldId,
                season = "spring"
            )
            // Update UI state
        }
    }
}
```

---

## 🐄 AgroVetRepository

### Key Functions
```kotlin
// Analyze symptoms and get triage
agroVetRepository.analyzeSymptoms(animalType, symptoms, severity)

// Get common diseases
agroVetRepository.getCommonDiseases(animalType, season)

// Get vaccination schedule
agroVetRepository.getVaccinationSchedule(animalType, age)

// Save/manage animal cases
agroVetRepository.saveAnimalCase(case)
agroVetRepository.getAllCases()

// Get emergency contacts
agroVetRepository.getEmergencyContacts(location)
```

### Triage Urgency Levels
- **CRITICAL** 🚨: Emergency - immediate vet required
- **HIGH** ⚠️: Vet within 24 hours
- **MEDIUM** ⏱️: Vet in 1-2 days
- **LOW** ℹ️: Monitor at home

### Critical Symptoms
- Severe bleeding, unable to stand, seizures
- Difficulty breathing, collapse, bloat
- Unresponsive

### Supported Animals
- Cattle/Cow (Mastitis, FMD, Anthrax, Brucellosis)
- Sheep (Foot Rot, Clostridial diseases)
- Goat (Pneumonia, CDT)
- Poultry (Newcastle Disease, IBD)

### TODO: ML Integration
- 🔲 ML model on veterinary diagnostic data
- 🔲 Image recognition for visual symptoms
- 🔲 Telemedicine platform integration
- 🔲 Location-based vet directory

### Usage Example
```kotlin
@HiltViewModel
class AgroVetViewModel @Inject constructor(
    private val agroVetRepository: AgroVetRepository
) : ViewModel() {
    
    fun diagnose(animalType: String, symptoms: List<String>, severity: Int) {
        viewModelScope.launch {
            val result = agroVetRepository.analyzeSymptoms(
                animalType = animalType,
                symptoms = symptoms,
                severity = severity
            )
            
            when (result.urgency) {
                UrgencyLevel.CRITICAL -> showEmergencyAlert()
                UrgencyLevel.HIGH -> scheduleVetVisit()
                else -> showSelfCareAdvice()
            }
        }
    }
}
```

---

## 🌿 PhotoDoctorRepository

### Key Functions
```kotlin
// Analyze plant photo (main function)
photoDoctorRepository.analyzePlantPhoto(bitmap, fieldId, cropType)

// Validate image quality
photoDoctorRepository.validateImageQuality(bitmap)

// Get diagnosis history
photoDoctorRepository.getAllDiagnoses()
photoDoctorRepository.getDiagnosesByField(fieldId)

// Get supported crops
photoDoctorRepository.getSupportedCrops()

// Get disease details
photoDoctorRepository.getDiseaseInfo(diseaseName)

// Check model status
photoDoctorRepository.isModelReady()
```

### PhotoDiagnosisResult
```kotlin
data class PhotoDiagnosisResult(
    val diagnosis: String,              // Disease name
    val confidence: Double,             // 0.0 to 1.0
    val severity: String,               // LOW/MEDIUM/HIGH/CRITICAL
    val diseaseType: String,            // fungal/bacterial/viral/pest/nutrient
    val treatments: List<TreatmentOption>,
    val recommendations: List<String>,
    val spreadRisk: String,
    val actionRequired: Boolean
)
```

### Mock Disease Database
- Healthy Plant
- Early Blight (Fungal)
- Late Blight (Fungal)
- Powdery Mildew (Fungal)
- Bacterial Wilt
- Mosaic Virus
- Nutrient Deficiency
- Pest Damage
- Sunburn

### Treatment Types
- **Organic**: Copper fungicides, Neem oil, Compost
- **Chemical**: Systemic fungicides (with precautions)
- **Cultural**: Pruning, removal, crop rotation
- **Biological**: Beneficial insects

### Supported Crops (15)
Wheat, Corn, Potato, Tomato, Apple, Grape, Strawberry, Cherry, Peach, Pepper, Soybean, Rice, Cotton, Barley, Alfalfa

### TODO: TFLite Integration
- 🔲 Add TensorFlow Lite dependencies
- 🔲 Download/train plant disease model
- 🔲 Implement image preprocessing
- 🔲 Initialize TFLite Interpreter
- 🔲 Run model inference
- 🔲 Post-process outputs

### Usage Example
```kotlin
@HiltViewModel
class PhotoDoctorViewModel @Inject constructor(
    private val photoDoctorRepository: PhotoDoctorRepository
) : ViewModel() {
    
    fun analyzePlant(bitmap: Bitmap, fieldId: String?) {
        viewModelScope.launch {
            val result = photoDoctorRepository.analyzePlantPhoto(
                bitmap = bitmap,
                fieldId = fieldId
            )
            
            // Show result in UI
            _diagnosisResult.value = result
            
            // Generate alert if severe
            if (result.severity in listOf("HIGH", "CRITICAL")) {
                createDiseaseAlert(result)
            }
        }
    }
}
```

---

## 🔧 TensorFlow Lite Integration

### 1. Add Dependencies (build.gradle.kts)
```kotlin
dependencies {
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0")
}
```

### 2. Add Model to Assets
```
app/src/main/assets/
  └── plant_disease_model.tflite
  └── labels.txt
```

### 3. Initialize Interpreter
```kotlin
private var interpreter: Interpreter? = null

init {
    val model = FileUtil.loadMappedFile(context, "plant_disease_model.tflite")
    interpreter = Interpreter(model)
}
```

### 4. Preprocess & Run Inference
```kotlin
val preprocessed = preprocessImage(bitmap) // Resize to 224x224, normalize
val output = Array(1) { FloatArray(numClasses) }
interpreter?.run(preprocessed, output)
```

### 5. Recommended Models
- **PlantVillage Dataset** (38 classes, 14 crops, ~95% accuracy)
- **Custom trained** for Kyrgyzstan crops
- **Cloud APIs**: Google Vision, Azure Custom Vision

---

## 🌐 Weather API Integration

### OpenWeather API
```kotlin
interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

// Usage in CropAdvisorRepository
val weatherData = weatherApiService.getCurrentWeather(
    lat = field.latitude,
    lon = field.longitude,
    apiKey = BuildConfig.WEATHER_API_KEY
)
```

---

## 📱 Hilt Dependency Injection

### RepositoryModule (Updated)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    // Existing repositories
    @Binds @Singleton abstract fun bindFarmerRepository(...): FarmerRepository
    @Binds @Singleton abstract fun bindFieldRepository(...): FieldRepository
    @Binds @Singleton abstract fun bindAlertRepository(...): AlertRepository
    
    // NEW: AI/ML repositories
    @Binds @Singleton abstract fun bindCropAdvisorRepository(...): CropAdvisorRepository
    @Binds @Singleton abstract fun bindAgroVetRepository(...): AgroVetRepository
    @Binds @Singleton abstract fun bindPhotoDoctorRepository(...): PhotoDoctorRepository
}
```

---

## 📂 File Structure

```
app/src/main/java/com/akyljer/
├── data/
│   ├── repository/
│   │   ├── CropAdvisorRepository.kt         ✅ NEW
│   │   ├── AgroVetRepository.kt             ✅ NEW
│   │   ├── PhotoDoctorRepository.kt         ✅ NEW
│   │   ├── FarmerRepository.kt
│   │   ├── FieldRepository.kt
│   │   ├── AlertRepository.kt
│   │   └── impl/
│   │       ├── CropAdvisorRepositoryImpl.kt     ✅ NEW
│   │       ├── AgroVetRepositoryImpl.kt         ✅ NEW
│   │       ├── PhotoDoctorRepositoryImpl.kt     ✅ NEW
│   │       ├── FarmerRepositoryImpl.kt
│   │       ├── FieldRepositoryImpl.kt
│   │       └── AlertRepositoryImpl.kt
│   └── seeding/
│       └── DataSeedingManager.kt
└── di/
    ├── DatabaseModule.kt
    └── RepositoryModule.kt                  ✅ UPDATED
```

---

## ✅ Testing Checklist

- [x] All repository interfaces created
- [x] All repository implementations with mock logic
- [x] Hilt DI bindings added
- [x] No compilation errors
- [x] TODO markers for ML integration
- [x] Documentation complete

---

## 🚀 Next Steps

1. **Update ViewModels**: Inject new repositories
2. **Create UI Screens**: Photo Doctor, AgroVet forms
3. **Camera Integration**: For plant photo capture
4. **Weather API**: Integrate OpenWeather
5. **TFLite Models**: Train/download plant disease models
6. **IoT Sensors**: Define integration protocol

---

## 📚 Additional Resources

- [TensorFlow Lite Guide](https://www.tensorflow.org/lite/guide)
- [PlantVillage Dataset](https://github.com/spMohanty/PlantVillage-Dataset)
- [OpenWeather API](https://openweathermap.org/api)
- [Android Camera X](https://developer.android.com/training/camerax)

---

**Status**: ✅ Step 6 Extended - Complete!  
**Next**: Connect repositories to ViewModels and UI
