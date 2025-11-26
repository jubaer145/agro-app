# Акыл Жер - Navigation Structure (Step 4 Complete)

## Navigation Graph

```
┌─────────────────────────────────────────────────────────────────┐
│                         MainActivity                             │
│                    (AkylJerAppScreen)                           │
│                                                                  │
│                      ┌──────────────┐                           │
│                      │  NavHost     │                           │
│                      │  (AppNavHost)│                           │
│                      └──────┬───────┘                           │
└─────────────────────────────┼──────────────────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │   Dashboard      │ ◄─── Start Destination
                    │   (Main Hub)     │
                    └────────┬─────────┘
                             │
          ┌──────────────────┼──────────────────┐
          │                  │                  │
          ▼                  ▼                  ▼
    ┌──────────┐      ┌─────────────┐    ┌──────────┐
    │ Profile  │      │   Fields    │    │  Smart   │
    │          │      │   List      │    │ Farming  │
    └──────────┘      └──────┬──────┘    └────┬─────┘
                             │                 │
                             ▼                 │
                      ┌──────────────┐         │
                      │   Field      │         │
                      │   Detail     │         │
                      │  (Add/Edit)  │         │
                      └──────────────┘         │
                                              │
                     ┌────────────────────────┼────────────────┐
                     │                        │                │
                     ▼                        ▼                ▼
              ┌─────────────┐         ┌─────────────┐   ┌──────────┐
              │    Crop     │         │    Photo    │   │  Others  │
              │   Advisor   │         │   Doctor    │   │          │
              └─────────────┘         └─────────────┘   └────┬─────┘
                                                              │
                              ┌───────────────────────────────┼────────┐
                              │                               │        │
                              ▼                               ▼        ▼
                        ┌──────────┐                    ┌─────────┐ ┌──────────┐
                        │ Weather  │                    │ AgroVet │ │  Alerts  │
                        │  & Risk  │                    │         │ │  /Tasks  │
                        └──────────┘                    └─────────┘ └──────────┘
                              
                                           ┌──────────┐
                                           │ Settings │
                                           └──────────┘
```

## Screen Details

### 1. Dashboard (Start)
- **Route**: `"dashboard"`
- **Purpose**: Main navigation hub
- **Features**:
  - Menu with all app sections
  - Quick access to all features
  - Welcome message
- **Navigation**: Can navigate to all top-level screens

### 2. Farmer Profile
- **Route**: `"farmer_profile"`
- **Purpose**: Manage farmer information
- **Features**:
  - Name, phone, location
  - Farm size input
  - Save profile
- **ViewModels**: `FarmerProfileViewModel`
- **Back**: Returns to Dashboard

### 3. Fields List
- **Route**: `"fields_list"`
- **Purpose**: View all farmer's fields
- **Features**:
  - List of fields with crop type and size
  - FAB to add new field
  - Click to edit field
  - Empty state
- **ViewModels**: `FieldsListViewModel`
- **Navigation**: 
  - Back to Dashboard
  - Forward to Field Detail

### 4. Field Detail
- **Route**: `"field_detail/{fieldId}"`
- **Parameters**: 
  - `fieldId`: String ("new" for create, ID for edit)
- **Purpose**: Add or edit field details
- **Features**:
  - Field name, crop type, size, location
  - Validation
  - Save button
- **ViewModels**: `FieldDetailViewModel`
- **Back**: Returns to Fields List

### 5. Smart Farming Hub
- **Route**: `"smart_farming"`
- **Purpose**: Hub for AI-powered features
- **Features**:
  - Navigation to Crop Advisor
  - Navigation to Photo Doctor
  - Feature descriptions
- **Navigation**:
  - Back to Dashboard
  - Forward to Advisor or Photo Doctor

### 6. Crop Advisor
- **Route**: `"advisor"`
- **Purpose**: AI recommendations and task generation
- **Features**:
  - Input form for crop/field data
  - Task list
  - Risk flags
  - Weather-based recommendations
- **ViewModels**: `AdvisorViewModel`
- **Technology**: Rule-based ML + Weather API

### 7. Photo Doctor
- **Route**: `"photo_doctor"`
- **Purpose**: On-device disease detection
- **Features**:
  - Camera/photo capture
  - TensorFlow Lite inference
  - Diagnosis results
  - History of analyses
- **ViewModels**: `PhotoDoctorViewModel`
- **Technology**: TensorFlow Lite

### 8. Weather & Risk
- **Route**: `"weather"`
- **Purpose**: Weather data and risk intelligence
- **Features**:
  - Current weather
  - Forecast
  - Drought/stress warnings
- **ViewModels**: `WeatherViewModel`
- **Technology**: Weather API

### 9. AgroVet
- **Route**: `"agrovet"`
- **Purpose**: Animal health triage
- **Features**:
  - Symptom checker
  - Basic diagnostics
  - Treatment suggestions
- **ViewModels**: `AgroVetViewModel`

### 10. Alerts / Tasks
- **Route**: `"alerts"`
- **Purpose**: Aggregated notifications and tasks
- **Features**:
  - Task list from Crop Advisor
  - Risk alerts
  - System notifications
- **ViewModels**: `AlertsViewModel`

### 11. Settings
- **Route**: `"settings"`
- **Purpose**: App configuration
- **Features**:
  - Notification toggles
  - Data sync settings
  - Language selection
  - About information
- **Back**: Returns to Dashboard

## Implementation Status

✅ All 11 screens implemented
✅ Navigation routes defined
✅ ViewModels created for stateful screens
✅ Back navigation configured
✅ Parameterized navigation (Field Detail)
✅ Material 3 UI components
✅ Hilt dependency injection

## Files Created/Modified

### Created:
1. `feature/profile/FarmerProfileScreen.kt`
2. `feature/profile/FarmerProfileViewModel.kt`
3. `feature/fields/FieldsListScreen.kt`
4. `feature/fields/FieldsListViewModel.kt`
5. `feature/fields/FieldDetailScreen.kt`
6. `feature/fields/FieldDetailViewModel.kt`
7. `feature/settings/SettingsScreen.kt`
8. `navigation/NavigationTest.kt`

### Modified:
1. `navigation/AppNavHost.kt` - Added all new routes
2. `feature/dashboard/DashboardScreen.kt` - Updated menu

### Already Existed:
1. `feature/smartfarming/SmartFarmingScreen.kt`
2. `feature/smartfarming/advisor/AdvisorScreen.kt`
3. `feature/photodoctor/PhotoDoctorScreen.kt`
4. `feature/weather/WeatherScreen.kt`
5. `feature/agrovet/AgroVetScreen.kt`
6. `feature/alerts/AlertsScreen.kt`

## Next Development Steps

1. **Data Layer**: Implement Room database entities and DAOs
2. **Repository Layer**: Create repositories and connect to ViewModels
3. **Weather API**: Integrate real weather service
4. **Photo Doctor**: Add camera integration and TFLite model
5. **Crop Advisor**: Implement rule engine and ML logic
6. **Localization**: Add Kyrgyz/Russian translations
7. **Testing**: Unit tests for ViewModels, UI tests for screens
