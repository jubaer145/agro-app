# Room Database Architecture - Акыл Жер MVP

## Complete Data Layer Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Presentation Layer                           │
│                    (Composables + ViewModels)                        │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            │ Inject DAOs via Hilt
                            │
┌───────────────────────────▼─────────────────────────────────────────┐
│                          Data Layer                                  │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │                     AkylZherDatabase                        │    │
│  │                   (Room Database v1)                        │    │
│  └───────────────────────┬────────────────────────────────────┘    │
│                          │                                          │
│         ┌────────────────┼────────────────┬──────────┐             │
│         │                │                │          │             │
│     ┌───▼────┐       ┌───▼────┐      ┌───▼────┐ ┌──▼───┐         │
│     │Profile │       │  Field │      │  Task  │ │ Alert│  ...    │
│     │  DAO   │       │   DAO  │      │   DAO  │ │  DAO │         │
│     └───┬────┘       └───┬────┘      └───┬────┘ └──┬───┘         │
│         │                │                │         │             │
│     ┌───▼────────┐   ┌───▼───────┐   ┌───▼──────┐ ┌───▼──────┐  │
│     │  Farmer    │   │   Field   │   │   Crop   │ │  Alert   │  │
│     │  Profile   │   │  Entity   │   │   Task   │ │  Entity  │  │
│     │  Entity    │   │           │   │  Entity  │ │          │  │
│     └────────────┘   └───────────┘   └──────────┘ └──────────┘  │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
                            │
                            │ Persisted to
                            ▼
                    ┌──────────────┐
                    │  SQLite DB   │
                    │  (On Device) │
                    └──────────────┘
```

## Entity Relationships

```
┌──────────────────────┐
│ FarmerProfileEntity  │
│ ─────────────────── │
│ • id (PK)            │
│ • name               │
│ • phone              │
│ • location           │
│ • farm_size_hectares │
└──────┬───────────────┘
       │ 1
       │
       │ N
┌──────▼───────────────┐         ┌──────────────────────┐
│    FieldEntity       │ 1       │  PhotoDiagnosisEntity│
│ ──────────────────  │◄────N───│ ──────────────────── │
│ • id (PK)            │         │ • id (PK)            │
│ • farmer_id (FK)     │         │ • field_id (FK)      │
│ • name               │         │ • image_path         │
│ • crop_type          │         │ • diagnosis_label    │
│ • size_hectares      │         │ • confidence         │
│ • location           │         │ • model_version      │
└──────┬───────────────┘         └──────────────────────┘
       │ 1
       │
       ├──────────────┐
       │ N            │ N
┌──────▼──────────┐ ┌─▼───────────────┐
│  CropTaskEntity │ │  AlertEntity    │
│ ─────────────── │ │ ─────────────── │
│ • id (PK)       │ │ • id (PK)       │
│ • field_id (FK) │ │ • field_id (FK) │
│ • title         │ │ • title         │
│ • description   │ │ • message       │
│ • task_type     │ │ • alert_type    │
│ • priority      │ │ • severity      │
│ • due_date      │ │ • is_read       │
│ • is_completed  │ │ • source        │
└─────────────────┘ └─────────────────┘

┌──────────────────────┐
│  AnimalCaseEntity    │
│ ──────────────────── │
│ • id (PK)            │
│ • farmer_id          │
│ • animal_type        │
│ • symptoms           │
│ • diagnosis          │
│ • severity           │
│ • status             │
└──────────────────────┘
```

## DAO Query Capabilities

### FarmerProfileDao
```
getProfile()                 → Flow<FarmerProfileEntity?>
getProfileById(id)          → FarmerProfileEntity?
insertProfile(profile)      → void
updateProfile(profile)      → void
getProfileCount()           → Int
```

### FieldDao
```
getFieldsByFarmer(id)       → Flow<List<FieldEntity>>
getFieldById(id)            → Flow<FieldEntity?>
getAllFields()              → Flow<List<FieldEntity>>
getFieldsByCropType(type)   → Flow<List<FieldEntity>>
insertField(field)          → void
updateField(field)          → void
deleteField(field)          → void
getTotalFieldArea(id)       → Double?
getFieldCount(id)           → Int
```

### CropTaskDao
```
getAllTasks()               → Flow<List<CropTaskEntity>>
getPendingTasks()           → Flow<List<CropTaskEntity>>
getCompletedTasks()         → Flow<List<CropTaskEntity>>
getTasksByField(id)         → Flow<List<CropTaskEntity>>
getHighPriorityTasks()      → Flow<List<CropTaskEntity>>
getOverdueTasks(time)       → Flow<List<CropTaskEntity>>
insertTask(task)            → void
markTaskCompleted(id)       → void
getPendingTaskCount()       → Int
```

### AlertDao
```
getAllAlerts()              → Flow<List<AlertEntity>>
getUnreadAlerts()           → Flow<List<AlertEntity>>
getAlertsBySeverity(sev)    → Flow<List<AlertEntity>>
getHighSeverityAlerts()     → Flow<List<AlertEntity>>
getAlertsRequiringAction()  → Flow<List<AlertEntity>>
insertAlert(alert)          → void
markAlertRead(id)           → void
dismissAlert(id)            → void
getUnreadAlertCount()       → Int
getUnreadAlertCountFlow()   → Flow<Int>
```

### AnimalCaseDao
```
getAllCases()               → Flow<List<AnimalCaseEntity>>
getActiveCases()            → Flow<List<AnimalCaseEntity>>
getCasesByFarmer(id)        → Flow<List<AnimalCaseEntity>>
getEmergencyCases()         → Flow<List<AnimalCaseEntity>>
insertCase(case)            → void
markCaseResolved(id)        → void
updateCaseStatus(id, stat)  → void
getActiveCaseCount(id)      → Int
```

### PhotoDiagnosisDao
```
getAllDiagnoses()           → Flow<List<PhotoDiagnosisEntity>>
getDiagnosesByField(id)     → Flow<List<PhotoDiagnosisEntity>>
getRecentDiagnoses(limit)   → Flow<List<PhotoDiagnosisEntity>>
getDiseaseDetections()      → Flow<List<PhotoDiagnosisEntity>>
getLatestDiagnosis()        → PhotoDiagnosisEntity?
insertDiagnosis(diagnosis)  → void
getDiagnosisCount()         → Int
```

## Hilt Dependency Injection Flow

```
┌─────────────────────────────────────────────────────┐
│              @HiltAndroidApp                         │
│              AkylJerApp                             │
│           (Application class)                        │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Provides dependencies
                     ▼
┌─────────────────────────────────────────────────────┐
│             @Module @InstallIn                       │
│             DatabaseModule                           │
│         (Singleton Component)                        │
│                                                      │
│  @Provides provideAkylZherDatabase(context)         │
│  @Provides provideFarmerProfileDao(db)              │
│  @Provides provideFieldDao(db)                      │
│  @Provides provideCropTaskDao(db)                   │
│  @Provides provideAlertDao(db)                      │
│  @Provides provideAnimalCaseDao(db)                 │
│  @Provides providePhotoDiagnosisDao(db)             │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Injected into
                     ▼
┌─────────────────────────────────────────────────────┐
│            @HiltViewModel                            │
│            SomeViewModel                             │
│                                                      │
│  @Inject constructor(                               │
│      private val dao: SomeDao                       │
│  ) : ViewModel()                                    │
└─────────────────────────────────────────────────────┘
```

## Data Flow: Create → Read → Update → Delete

### CREATE
```
User Input (Composable)
    ↓
ViewModel.createField()
    ↓
viewModelScope.launch {
    fieldDao.insertField(entity)
}
    ↓
Room inserts into SQLite
    ↓
Flow emits updated list
    ↓
UI updates automatically
```

### READ (Reactive)
```
ViewModel initializes
    ↓
val fields = fieldDao.getFieldsByFarmer(id)
    ↓
Room queries SQLite
    ↓
Flow<List<FieldEntity>>
    ↓
.collectAsState() in Composable
    ↓
LazyColumn displays fields
    ↓
(Auto-updates when DB changes)
```

### UPDATE
```
User edits field (Composable)
    ↓
ViewModel.updateField(id, data)
    ↓
viewModelScope.launch {
    val existing = dao.getFieldByIdOnce(id)
    dao.updateField(existing.copy(...))
}
    ↓
Room updates SQLite
    ↓
Flow emits updated data
    ↓
UI refreshes
```

### DELETE
```
User clicks delete (Composable)
    ↓
ViewModel.deleteField(id)
    ↓
viewModelScope.launch {
    fieldDao.deleteFieldById(id)
}
    ↓
Room deletes from SQLite
    ↓
CASCADE deletes related tasks/alerts
    ↓
Flow emits updated list
    ↓
UI updates (field removed)
```

## File Structure

```
app/src/main/java/com/akyljer/
├── data/
│   └── local/
│       ├── entity/
│       │   ├── FarmerProfileEntity.kt
│       │   ├── FieldEntity.kt
│       │   ├── CropTaskEntity.kt
│       │   ├── AlertEntity.kt
│       │   ├── AnimalCaseEntity.kt
│       │   └── PhotoDiagnosisEntity.kt
│       ├── dao/
│       │   ├── FarmerProfileDao.kt
│       │   ├── FieldDao.kt
│       │   ├── CropTaskDao.kt
│       │   ├── AlertDao.kt
│       │   ├── AnimalCaseDao.kt
│       │   └── PhotoDiagnosisDao.kt
│       ├── mapper/
│       │   └── EntityMappers.kt
│       └── AkylZherDatabase.kt
├── di/
│   └── DatabaseModule.kt
└── AkylJerApp.kt (@HiltAndroidApp)
```

## Key Benefits

✅ **Offline-First**: All data persisted locally
✅ **Reactive**: Flow-based queries auto-update UI
✅ **Type-Safe**: Kotlin with Room compile-time checks
✅ **Relationships**: Foreign keys with CASCADE
✅ **Performance**: Indexed queries
✅ **DI**: Clean Hilt injection
✅ **Coroutines**: Async operations
✅ **Migration**: Schema versioning support
✅ **Testing**: In-memory database support

---

## Next: Repository Layer

Create repositories to:
- Add business logic
- Handle multiple data sources (local + remote)
- Provide clean API to ViewModels
- Add caching strategies
- Handle error cases
