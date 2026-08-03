package com.mastermind.intellidrain.data.model

// ---- Confirmed live: POST /flood-risk/ ----
data class FloodRiskRequest(
    val latitude: Double,
    val longitude: Double,
    val blockage_ratio: Double
)

data class FloodRiskResponse(
    val status: String,
    val coordinates: Coordinates,
    val environmental_metrics: EnvironmentalMetrics,
    val risk_analysis: RiskAnalysis
)

data class Coordinates(val lat: Double, val lon: Double)

data class EnvironmentalMetrics(
    val rainfall_last_hour_mm: Double,
    val normalized_intensity: Double,
    val humidity_pct: Int,
    val temperature_c: Double
)

data class RiskAnalysis(
    val input_blockage_ratio: Double,
    val computed_risk_score: Double,
    val flood_risk_level: String, // "LOW", "ELEVATED", "CRITICAL"
    val dispatch_action: String
)

// ---- Confirmed live: POST /detect/ ----
data class DetectRequest(val image_url: String)

data class DetectResponse(
    val status: String,
    val message: String,
    val blockage_ratio: Double
)

// ---- Pending from teammate: GET /drains/ ----
data class Drain(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val area_name: String,
    val risk_level: String,
    val blockage_ratio: Double,
    val last_inspection_date: String
)

data class DrainDetail(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val area_name: String,
    val risk_level: String,
    val blockage_ratio: Double,
    val last_inspection_date: String,
    val rainfall_last_hour_mm: Double,
    val humidity_pct: Int,
    val temperature_c: Double,
    val dispatch_action: String,
    val last_cleaning_date: String?
)

data class InspectionRecord(
    val date: String,
    val blockage_ratio: Double,
    val risk_level: String,
    val status: String
)

// ---- Derived client-side from Drain data — not a separate backend endpoint ----
data class Alert(
    val drainId: Int,
    val title: String,
    val priority: String, // "CRITICAL" or "ELEVATED"
    val location: String,
    val time: String,
    val recommendedAction: String
)