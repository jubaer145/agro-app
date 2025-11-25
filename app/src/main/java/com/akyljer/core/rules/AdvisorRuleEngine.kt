package com.akyljer.core.rules

import com.akyljer.core.model.CropTask
import com.akyljer.core.model.FarmerInput
import com.akyljer.core.model.RiskFlag
import com.akyljer.core.model.RiskLevel
import com.akyljer.core.model.WeatherForecast
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class AdviceResult(
    val tasks: List<CropTask>,
    val risks: List<RiskFlag>
)

@Singleton
class AdvisorRuleEngine @Inject constructor() {

    fun generateAdvice(input: FarmerInput, weather: WeatherForecast?): AdviceResult {
        val tasks = mutableListOf<CropTask>()
        val risks = mutableListOf<RiskFlag>()

        val riskLevel = when {
            weather?.maxTempC ?: 0.0 >= 32 -> RiskLevel.HIGH
            weather?.precipitationMm ?: 0.0 < 1.0 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val irrigationNeeded = (weather?.precipitationMm ?: 0.0) < 2.0
        if (irrigationNeeded) {
            tasks += CropTask(
                id = UUID.randomUUID().toString(),
                title = "Irrigation",
                description = "Water ${input.crop.ifBlank { "crop" }} within 48h.",
                dueDateMillis = System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000,
                riskLevel = riskLevel
            )
        }

        if (weather?.maxTempC ?: 0.0 > 30) {
            risks += RiskFlag(
                id = UUID.randomUUID().toString(),
                label = "Heat stress",
                level = riskLevel,
                details = "High temperature expected; monitor wilting and adjust irrigation."
            )
        }

        if (input.growthStage.lowercase().contains("seedling")) {
            tasks += CropTask(
                id = UUID.randomUUID().toString(),
                title = "Early stage care",
                description = "Inspect seedlings for pests and apply bio-control if needed.",
                dueDateMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
                riskLevel = RiskLevel.LOW
            )
        }

        return AdviceResult(tasks = tasks, risks = risks)
    }
}
