# 🌾 Акыл Жер (Smart Farming) - Android App

<div align="center">

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-purple)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)
![AI](https://img.shields.io/badge/AI-TensorFlow%20Lite-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

**An AI-powered offline-first smart farming application for Kyrgyzstan farmers**

[Features](#-features) • [Architecture](#-architecture) • [Setup](#-setup) • [Build](#-build--install) • [Tech Stack](#-tech-stack)

</div>

---

## 📋 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Setup](#-setup)
- [Build & Install](#-build--install)
- [Tech Stack](#-tech-stack)
- [Development](#-development)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 About

**Акыл Жер** (Smart Land) is an intelligent farming assistant designed specifically for farmers in Kyrgyzstan. The app provides AI-powered plant disease detection, crop management, weather intelligence, and animal health triage - all working **100% offline** to serve remote farming communities.

### Key Highlights

- 🤖 **AI Plant Disease Detection** - Real-time disease diagnosis using TensorFlow Lite
- 📱 **Offline-First** - All core features work without internet connection
- 🌾 **Local Crops Support** - Tomato, Potato, Wheat, Corn, and more
- 🇰🇬 **Built for Kyrgyzstan** - Localized for Kyrgyz and Russian languages
- 🔒 **Privacy-First** - All data stays on device

---

## ✨ Features

### 1. 🔬 Photo Doctor (AI Disease Detection)
- **Real-time camera integration** using CameraX
- **TensorFlow Lite model** for offline plant disease detection
- **21+ disease classifications** including:
  - Tomato diseases (Early Blight, Late Blight, Leaf Mold, etc.)
  - Potato diseases (Early Blight, Late Blight)
  - Wheat diseases (Rust, Septoria)
  - Corn diseases (Common Rust, Gray Leaf Spot, Northern Leaf Blight)
- **Treatment recommendations** and prevention tips
- **Diagnosis history** with local storage
- **Confidence scores** and severity levels

### 2. 🌾 Smart Farming Hub
- **Crop Advisor** - Personalized crop recommendations
- **Field Management** - Track multiple fields and crops
- **Task Management** - Planting, watering, fertilizing schedules
- **Yield Tracking** - Monitor crop performance

### 3. 🌤️ Weather & Risk Intelligence
- Weather forecasts (when online)
- Risk alerts for diseases and pests
- Climate-based recommendations

### 4. 🐄 AgroVet (Animal Health)
- Animal health triage
- Disease symptom checker
- Treatment protocols

### 5. 📊 Dashboard
- Quick overview of farm status
- Recent alerts and tasks
- Field summaries
- Action items

---

## 🏗️ Architecture

The app follows **Clean Architecture** principles with **MVVM** pattern:

```
┌─────────────────────────────────────────────────┐
│                  UI Layer (Compose)              │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐│
│  │  Screens   │  │ ViewModels │  │   Theme    ││
│  └────────────┘  └────────────┘  └────────────┘│
└─────────────────────────────────────────────────┘
                      ▼
┌─────────────────────────────────────────────────┐
│              Domain Layer                        │
│  ┌────────────┐  ┌────────────┐                │
│  │ Use Cases  │  │   Models   │                │
│  └────────────┘  └────────────┘                │
└─────────────────────────────────────────────────┘
                      ▼
┌─────────────────────────────────────────────────┐
│              Data Layer                          │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐│
│  │Repository  │  │    Room    │  │ TFLite AI  ││
│  └────────────┘  └────────────┘  └────────────┘│
└─────────────────────────────────────────────────┘
```

### Key Architectural Components

- **UI Layer**: Jetpack Compose for modern, declarative UI
- **ViewModel**: State management with Kotlin Flows
- **Repository Pattern**: Single source of truth for data
- **Room Database**: Local data persistence
- **Hilt**: Dependency injection
- **TensorFlow Lite**: On-device AI inference

---

## 📂 Project Structure

```
agro-app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/akyljer/
│   │       │   ├── ai/                      # AI/ML components
│   │       │   │   ├── PlantDiseaseDetector.kt
│   │       │   │   └── DiseaseInfoMapper.kt
│   │       │   ├── data/                    # Data layer
│   │       │   │   ├── local/
│   │       │   │   │   ├── dao/             # Room DAOs
│   │       │   │   │   ├── entity/          # Database entities
│   │       │   │   │   └── database/        # Database setup
│   │       │   │   └── repository/          # Repository implementations
│   │       │   │       └── impl/
│   │       │   ├── di/                      # Dependency injection
│   │       │   │   ├── DatabaseModule.kt
│   │       │   │   ├── RepositoryModule.kt
│   │       │   │   └── AIModule.kt
│   │       │   ├── ui/                      # UI layer
│   │       │   │   ├── features/            # Feature modules
│   │       │   │   │   ├── photodoctor/     # Photo Doctor screens
│   │       │   │   │   ├── dashboard/       # Dashboard
│   │       │   │   │   ├── fields/          # Field management
│   │       │   │   │   └── ...
│   │       │   │   └── theme/               # App theme
│   │       │   ├── navigation/              # Navigation setup
│   │       │   ├── AkylJerApp.kt           # Application class
│   │       │   └── MainActivity.kt          # Main activity
│   │       ├── assets/                      # AI models & labels
│   │       │   ├── plant_disease_model.tflite  # TFLite model (16.8 MB)
│   │       │   └── labels.txt               # Disease labels
│   │       ├── res/                         # Resources
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts                     # App-level build config
├── gradle/                                  # Gradle wrapper
├── build.gradle.kts                         # Project-level build config
├── settings.gradle.kts                      # Gradle settings
└── README.md                                # This file
```

---

## 🚀 Setup

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 17 or higher
- **Android SDK**: API 24+ (Android 7.0+)
- **Gradle**: 8.2+ (included in wrapper)
- **Device/Emulator**: Android 7.0 (API 24) or higher

### Clone Repository

```bash
git clone https://github.com/yourusername/agro-app.git
cd agro-app
```

### Open in Android Studio

1. Launch Android Studio
2. Click **"Open"**
3. Navigate to the `agro-app` directory
4. Click **"OK"**
5. Wait for Gradle sync to complete

### Download Dependencies

```bash
# Gradle will automatically download dependencies
# Or manually trigger:
./gradlew build --refresh-dependencies
```

---

## 🔨 Build & Install

### Build Debug APK

```bash
# Clean previous builds
./gradlew clean

# Build debug APK
./gradlew assembleDebug
```

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device

#### Via Android Studio
1. Connect Android device via USB (enable USB debugging)
2. Click **Run** button (▶️) or press `Shift + F10`
3. Select target device
4. App will build and install automatically

#### Via Command Line
```bash
# Build and install in one command
./gradlew installDebug

# Or manually install APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Build Release APK

```bash
# Build release APK (unsigned)
./gradlew assembleRelease

# Build and sign release APK
./gradlew bundleRelease
```

---

## 🛠️ Tech Stack

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.20 | Primary language |
| **Jetpack Compose** | 1.5.4 | Modern UI framework |
| **Coroutines** | 1.7.3 | Async programming |
| **Flow** | 1.7.3 | Reactive streams |

### Architecture Components

| Component | Version | Purpose |
|-----------|---------|---------|
| **ViewModel** | 2.6.2 | UI state management |
| **Navigation** | 2.7.5 | Screen navigation |
| **Room** | 2.6.0 | Local database |
| **Hilt** | 2.48 | Dependency injection |
| **Lifecycle** | 2.6.2 | Lifecycle awareness |

### AI/ML

| Library | Version | Purpose |
|---------|---------|---------|
| **TensorFlow Lite** | 2.14.0 | AI inference engine |
| **TFLite Support** | 0.4.4 | Image preprocessing |

### Camera & Media

| Library | Version | Purpose |
|---------|---------|---------|
| **CameraX** | 1.3.0 | Camera integration |
| **ExifInterface** | 1.3.6 | Image metadata |
| **Coil** | 2.5.0 | Image loading |

### UI Components

| Library | Version | Purpose |
|---------|---------|---------|
| **Material3** | 1.1.2 | Material Design 3 |
| **Accompanist** | 0.34.0 | Compose utilities |

---

## 💻 Development

### Run Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Check Code Quality

```bash
# Run linter
./gradlew lint

# Format code
./gradlew ktlintFormat
```

### Debug Logs

```bash
# View app logs
adb logcat | grep "AkylJer"

# View Photo Doctor logs
adb logcat | grep "PhotoDoctor"

# View TFLite logs
adb logcat | grep "PlantDiseaseDetector"
```

### Common Commands

```bash
# Clean build
./gradlew clean

# Build debug
./gradlew assembleDebug

# Install debug
./gradlew installDebug

# Uninstall
./gradlew uninstallDebug
# or
adb uninstall com.akyljer

# Check dependencies
./gradlew dependencies

# Check for updates
./gradlew dependencyUpdates
```

---

## 📱 Features in Detail

### Photo Doctor - AI Disease Detection

**Technical Implementation:**
- **Model**: TensorFlow Lite (16.8 MB, Float32)
- **Input**: 224x224 RGB images
- **Output**: 21 disease classifications
- **Inference Time**: ~300-600ms on mid-range device
- **Accuracy**: ~85-95% confidence on trained classes

**Supported Diseases:**
- ✅ Tomato: Early Blight, Late Blight, Leaf Mold, Septoria Leaf Spot, Spider Mites, Target Spot, Mosaic Virus, Yellow Leaf Curl Virus, Bacterial Spot
- ✅ Potato: Early Blight, Late Blight, Healthy
- ✅ Wheat: Healthy, Rust, Septoria
- ✅ Corn: Common Rust, Gray Leaf Spot, Healthy, Northern Leaf Blight

**User Flow:**
1. Tap "📸 Analyze Sample Photo"
2. Camera opens (CameraX)
3. Take photo of affected plant part
4. AI analyzes on-device (~0.5s)
5. View diagnosis, confidence, treatments
6. Save to history

---

## 🔒 Privacy & Security

- ✅ **No data collection** - All data stays on device
- ✅ **No analytics** - No tracking or telemetry
- ✅ **Offline-first** - Works without internet
- ✅ **Local storage** - Room database (SQLite)
- ✅ **No cloud sync** - User controls their data

---

## 🌍 Localization

**Supported Languages:**
- 🇰🇬 Kyrgyz (Kyrgyz language)
- 🇷🇺 Russian (Primary UI language)
- 🇬🇧 English (Development/fallback)

**Add New Language:**
1. Create `values-{lang}/strings.xml`
2. Translate all strings
3. Update app configuration

---

## 📊 Database Schema

**Entities:**
- `FarmerProfileEntity` - Farmer information
- `FieldEntity` - Farm fields
- `CropTaskEntity` - Farming tasks
- `AlertEntity` - Alerts and notifications
- `PhotoDiagnosisEntity` - Disease diagnosis history
- `AnimalCaseEntity` - Animal health records

---

## 🐛 Troubleshooting

### Build Issues

**Problem**: Gradle sync fails
```bash
# Solution: Clear Gradle cache
./gradlew clean
rm -rf .gradle
./gradlew build
```

**Problem**: OutOfMemoryError during build
```bash
# Solution: Increase heap size in gradle.properties
org.gradle.jvmargs=-Xmx4096m
```

### Runtime Issues

**Problem**: Camera not opening
- Check permissions in AndroidManifest.xml
- Grant camera permission manually: `adb shell pm grant com.akyljer android.permission.CAMERA`

**Problem**: TFLite model not loading
- Verify `plant_disease_model.tflite` exists in `app/src/main/assets/`
- Check file size: should be ~16.8 MB
- Look for errors: `adb logcat | grep PlantDiseaseDetector`

**Problem**: App crashes on launch
- Check logs: `adb logcat | grep AndroidRuntime`
- Verify Hilt setup is correct
- Ensure all dependencies are synced

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable/function names
- Add comments for complex logic
- Write tests for new features

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

**Developed for Kyrgyzstan Agro Hackathon 2025**

---

## 📞 Support

For issues, questions, or feature requests:
- Open an [issue](https://github.com/yourusername/agro-app/issues)
- Contact: your.email@example.com

---

## 🙏 Acknowledgments

- TensorFlow Lite team for AI framework
- Jetpack Compose team for UI toolkit
- PlantVillage dataset contributors
- Kyrgyzstan farming community

---

## 📈 Roadmap

### Version 2.0 (Planned)
- [ ] More crop types (Rice, Barley, Alfalfa)
- [ ] Custom model training for local crops
- [ ] GPU acceleration for faster inference
- [ ] Multi-language support (Kyrgyz, Uzbek)
- [ ] Cloud sync (optional)
- [ ] Community features (share diagnoses)

### Version 1.1 (In Progress)
- [x] Photo Doctor with TFLite
- [x] Offline operation
- [x] Camera integration
- [ ] Weather API integration
- [ ] AgroVet database
- [ ] Field mapping with GPS

---

<div align="center">

**Built with ❤️ for farmers in Kyrgyzstan 🇰🇬**

⭐ Star this repo if you find it useful!

</div>
