# Step 5 Quick Reference - Room Database

## Database Structure

### Entities (6)
```kotlin
FarmerProfileEntity    // Farmer info
FieldEntity           // Fields/lands
CropTaskEntity        // AI/manual tasks
AlertEntity           // Risk alerts
AnimalCaseEntity      // AgroVet cases
PhotoDiagnosisEntity  // Photo Doctor results
```

### DAOs (6)
```kotlin
FarmerProfileDao      // Profile CRUD
FieldDao              // Field management
CropTaskDao           // Task operations
AlertDao              // Alert handling
AnimalCaseDao         // Animal health
PhotoDiagnosisDao     // Diagnosis history
```

## Quick Start: Using DAOs in ViewModels

### 1. Inject DAO
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val fieldDao: FieldDao
) : ViewModel()
```

### 2. Query Data (Reactive with Flow)
```kotlin
val fields: Flow<List<FieldEntity>> = fieldDao.getFieldsByFarmer(farmerId)

// Collect in UI
val fieldsState by fields.collectAsState(initial = emptyList())
```

### 3. Insert Data
```kotlin
viewModelScope.launch {
    fieldDao.insertField(
        FieldEntity(
            id = UUID.randomUUID().toString(),
            farmerId = farmerId,
            name = "North Field",
            cropType = "Wheat",
            sizeHectares = 5.5
        )
    )
}
```

### 4. Update Data
```kotlin
viewModelScope.launch {
    val field = fieldDao.getFieldByIdOnce(fieldId)
    field?.let {
        fieldDao.updateField(
            it.copy(cropType = "Barley", updatedAt = System.currentTimeMillis())
        )
    }
}
```

### 5. Delete Data
```kotlin
viewModelScope.launch {
    fieldDao.deleteFieldById(fieldId)
}
```

## Common Patterns

### Pattern 1: Load and Display List
```kotlin
@HiltViewModel
class FieldsViewModel @Inject constructor(
    private val fieldDao: FieldDao
) : ViewModel() {
    
    fun getFields(farmerId: String) = fieldDao.getFieldsByFarmer(farmerId)
        .map { entities -> entities.map { it.toDomainModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

// In Composable
@Composable
fun FieldsScreen(viewModel: FieldsViewModel) {
    val fields by viewModel.getFields(farmerId).collectAsState()
    
    LazyColumn {
        items(fields) { field ->
            FieldCard(field)
        }
    }
}
```

### Pattern 2: Save Form Data
```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: FarmerProfileDao
) : ViewModel() {
    
    fun saveProfile(name: String, phone: String, location: String) {
        viewModelScope.launch {
            profileDao.insertProfile(
                FarmerProfileEntity(
                    id = "farmer_1", // Or get from existing profile
                    name = name,
                    phone = phone,
                    location = location,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
```

### Pattern 3: Count/Statistics
```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val fieldDao: FieldDao,
    private val taskDao: CropTaskDao,
    private val alertDao: AlertDao
) : ViewModel() {
    
    val pendingTaskCount = taskDao.getPendingTaskCount()
    val unreadAlertCount = alertDao.getUnreadAlertCountFlow()
    val fieldCount = fieldDao.getFieldCount(farmerId)
}
```

### Pattern 4: Complex Queries
```kotlin
// Get high priority overdue tasks
val criticalTasks = taskDao.getHighPriorityTasks()
    .map { tasks -> 
        tasks.filter { it.dueDate < System.currentTimeMillis() }
    }

// Get unread high severity alerts
val urgentAlerts = alertDao.getHighSeverityAlerts()
    .map { alerts -> alerts.filter { !it.isRead } }
```

## Entity to Domain Model Conversion

```kotlin
// Single entity
val domainModel = entity.toDomainModel()

// List of entities
val domainModels = entities.toFieldModels()

// Domain to entity
val entity = domainModel.toEntity()
```

## Database Initialization

### Automatic (via Hilt) ✅
Database is automatically created when you inject a DAO:
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val dao: SomeDao  // Database auto-initialized
) : ViewModel()
```

### Manual (if needed)
```kotlin
val database = Room.databaseBuilder(
    context,
    AkylZherDatabase::class.java,
    AkylZherDatabase.DATABASE_NAME
).build()

val dao = database.someDao()
```

## Useful DAO Methods

### FarmerProfileDao
```kotlin
getProfile()                    // Current profile (Flow)
insertProfile(profile)          // Save profile
getProfileCount()               // Check if exists
```

### FieldDao
```kotlin
getFieldsByFarmer(id)          // All fields (Flow)
getFieldById(id)               // Single field (Flow)
getTotalFieldArea(id)          // Sum of hectares
getFieldCount(id)              // Number of fields
```

### CropTaskDao
```kotlin
getPendingTasks()              // Incomplete tasks (Flow)
getOverdueTasks(time)          // Past due date (Flow)
markTaskCompleted(id)          // Mark as done
getPendingTaskCount()          // Count
```

### AlertDao
```kotlin
getUnreadAlerts()              // Unread only (Flow)
getHighSeverityAlerts()        // Critical alerts (Flow)
markAlertRead(id)              // Mark single
markAllAlertsRead()            // Mark all
getUnreadAlertCount()          // Count
```

### PhotoDiagnosisDao
```kotlin
getRecentDiagnoses(10)         // Last N (Flow)
getLatestDiagnosis()           // Most recent
getDiseaseDetections()         // Excluding healthy (Flow)
insertDiagnosis(diagnosis)     // Save result
```

### AnimalCaseDao
```kotlin
getActiveCases()               // Unresolved (Flow)
getEmergencyCases()            // Severe/Emergency (Flow)
markCaseResolved(id)           // Close case
getActiveCaseCount(id)         // Count
```

## Data Flow Pattern

```
UI Layer (Composable)
    ↓ collectAsState()
ViewModel (with Flow)
    ↓ stateIn() / map()
DAO (Flow queries)
    ↓
Room Database
```

## Best Practices

1. **Use Flow for reactive UI**: DAO queries return Flow
2. **Use suspend for one-time operations**: Insert, update, delete
3. **Convert in ViewModel**: Use mappers to convert entities to domain models
4. **Handle errors**: Wrap DB operations in try-catch
5. **Use viewModelScope**: For coroutine launches
6. **Collect in UI**: Use collectAsState() in Composables

## Example: Complete CRUD Flow

```kotlin
@HiltViewModel
class FieldViewModel @Inject constructor(
    private val fieldDao: FieldDao
) : ViewModel() {
    
    // READ (List)
    fun getFields(farmerId: String) = fieldDao.getFieldsByFarmer(farmerId)
    
    // READ (Single)
    fun getField(id: String) = fieldDao.getFieldById(id)
    
    // CREATE
    fun createField(name: String, cropType: String, size: Double, farmerId: String) {
        viewModelScope.launch {
            fieldDao.insertField(
                FieldEntity(
                    id = UUID.randomUUID().toString(),
                    farmerId = farmerId,
                    name = name,
                    cropType = cropType,
                    sizeHectares = size
                )
            )
        }
    }
    
    // UPDATE
    fun updateField(id: String, name: String, cropType: String, size: Double) {
        viewModelScope.launch {
            fieldDao.getFieldByIdOnce(id)?.let { existing ->
                fieldDao.updateField(
                    existing.copy(
                        name = name,
                        cropType = cropType,
                        sizeHectares = size,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }
    
    // DELETE
    fun deleteField(id: String) {
        viewModelScope.launch {
            fieldDao.deleteFieldById(id)
        }
    }
}
```

## Migration Path

### From Placeholder Data to Real DB

**Before (Step 4):**
```kotlin
val fields = listOf(
    FieldListItem(id = "1", name = "Field 1", cropType = "Wheat", size = 5.5)
)
```

**After (Step 5):**
```kotlin
val fields: Flow<List<FieldEntity>> = fieldDao.getFieldsByFarmer(farmerId)
```

---

## Summary

- ✅ 6 entities covering all MVP features
- ✅ 6 DAOs with comprehensive query methods
- ✅ Hilt DI for easy injection
- ✅ Flow-based reactive queries
- ✅ Entity mappers for clean architecture
- ✅ Ready to use in ViewModels

**Next**: Create repositories and integrate with ViewModels!
