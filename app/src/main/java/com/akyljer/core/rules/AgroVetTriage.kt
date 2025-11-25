package com.akyljer.core.rules

import com.akyljer.core.model.AgroVetAnswer
import com.akyljer.core.model.RiskLevel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgroVetTriage @Inject constructor() {
    fun triage(symptoms: List<String>): AgroVetAnswer {
        val lowered = symptoms.joinToString().lowercase()
        return when {
            "diarrhea" in lowered -> AgroVetAnswer(
                condition = "Possible bacterial infection",
                urgency = RiskLevel.HIGH,
                recommendation = "Isolate animal, provide clean water, contact vet if worsens."
            )
            "cough" in lowered || "fever" in lowered -> AgroVetAnswer(
                condition = "Respiratory issue",
                urgency = RiskLevel.MEDIUM,
                recommendation = "Check temperature, improve ventilation, consult vet."
            )
            else -> AgroVetAnswer(
                condition = "General check",
                urgency = RiskLevel.LOW,
                recommendation = "Monitor symptoms and ensure hydration."
            )
        }
    }
}
