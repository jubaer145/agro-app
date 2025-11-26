package com.akyljer.ui.features.photodoctor

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akyljer.data.repository.PhotoDiagnosisResult

/**
 * Diagnosis Result Display Screen
 * Shows AI analysis results with treatment recommendations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultScreen(
    result: PhotoDiagnosisResult,
    bitmap: Bitmap?,
    onRetake: () -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnosis Result") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, "Close")
                    }
                },
                actions = {
                    IconButton(onClick = onRetake) {
                        Icon(Icons.Default.Refresh, "Retake Photo")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = getSeverityColor(result.severity).copy(alpha = 0.1f)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image preview
            item {
                bitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Captured plant",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            // Main diagnosis card
            item {
                DiagnosisCard(result)
            }
            
            // Confidence and metadata
            item {
                MetadataCard(result)
            }
            
            // Action required alert
            if (result.actionRequired) {
                item {
                    ActionRequiredCard(result.severity)
                }
            }
            
            // Recommendations
            item {
                RecommendationsCard(result.recommendations)
            }
            
            // Treatment options
            if (result.treatments.isNotEmpty()) {
                item {
                    TreatmentsCard(result.treatments)
                }
            }
            
            // Prevention tips
            if (result.preventionTips.isNotEmpty()) {
                item {
                    PreventionTipsCard(result.preventionTips)
                }
            }
            
            // Alternative diagnoses
            if (result.alternativeDiagnoses.isNotEmpty()) {
                item {
                    AlternativeDiagnosesCard(result.alternativeDiagnoses)
                }
            }
            
            // Disclaimer
            item {
                DisclaimerCard()
            }
        }
    }
}

@Composable
fun DiagnosisCard(result: PhotoDiagnosisResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getSeverityColor(result.severity).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getDiseaseIcon(result.diseaseType),
                    contentDescription = null,
                    tint = getSeverityColor(result.severity),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        result.diagnosis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Disease Type: ${result.diseaseType.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Severity badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getSeverityColor(result.severity)
                ) {
                    Text(
                        result.severity.uppercase(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    "Affects: ${result.affectedPart.replace("_", " ").replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MetadataCard(result: PhotoDiagnosisResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetadataItem(
                    icon = Icons.Default.Percent,
                    label = "Confidence",
                    value = "${(result.confidence * 100).toInt()}%"
                )
                MetadataItem(
                    icon = Icons.Default.Speed,
                    label = "Spread Risk",
                    value = result.spreadRisk.replaceFirstChar { it.uppercase() }
                )
                MetadataItem(
                    icon = Icons.Default.Timer,
                    label = "Analyzed in",
                    value = "${result.processingTimeMs}ms"
                )
            }
        }
    }
}

@Composable
fun MetadataItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActionRequiredCard(severity: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "⚠️ Immediate Action Required",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    "This ${severity.lowercase()} severity issue needs your attention.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun RecommendationsCard(recommendations: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("• ", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        recommendation,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TreatmentsCard(treatments: List<com.akyljer.data.repository.TreatmentOption>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Treatment Options",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            treatments.forEach { treatment ->
                TreatmentItem(treatment)
                if (treatment != treatments.last()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
fun TreatmentItem(treatment: com.akyljer.data.repository.TreatmentOption) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                treatment.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (treatment.organic) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF4CAF50).copy(alpha = 0.2f)
                ) {
                    Text(
                        "ORGANIC",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            treatment.description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            TreatmentBadge("Type", treatment.type)
            Spacer(modifier = Modifier.width(8.dp))
            TreatmentBadge("Effectiveness", treatment.effectiveness)
            Spacer(modifier = Modifier.width(8.dp))
            TreatmentBadge("Cost", treatment.cost)
        }
    }
}

@Composable
fun TreatmentBadge(label: String, value: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PreventionTipsCard(tips: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Prevention Tips",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            tips.forEach { tip ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("✓ ", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4CAF50))
                    Text(
                        tip,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun AlternativeDiagnosesCard(alternatives: List<com.akyljer.data.repository.AlternativeDiagnosis>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Other Possible Diagnoses",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            alternatives.forEach { alt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        alt.diagnosis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${(alt.confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun DisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "This AI diagnosis is for informational purposes only. Consult with an agricultural expert for professional advice.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Helper functions
fun getSeverityColor(severity: String): Color {
    return when (severity.uppercase()) {
        "CRITICAL" -> Color(0xFFD32F2F)
        "HIGH" -> Color(0xFFFF6B6B)
        "MEDIUM" -> Color(0xFFFFB74D)
        "LOW" -> Color(0xFF66BB6A)
        else -> Color.Gray
    }
}

fun getDiseaseIcon(diseaseType: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (diseaseType.lowercase()) {
        "fungal" -> Icons.Default.CloudCircle
        "bacterial" -> Icons.Default.BugReport
        "viral" -> Icons.Default.Coronavirus
        "pest" -> Icons.Default.PestControl
        "nutrient" -> Icons.Default.Science
        "environmental" -> Icons.Default.WbSunny
        "none" -> Icons.Default.CheckCircle
        else -> Icons.Default.Help
    }
}
