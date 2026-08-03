package com.mastermind.intellidrain.data.repository

import com.mastermind.intellidrain.data.model.*
import com.mastermind.intellidrain.data.network.RetrofitClient
import kotlinx.coroutines.delay

class DrainRepository {

    private val api = RetrofitClient.apiService

    companion object {
        // Flip this to false the day the backend's GET endpoints go live
        const val USE_MOCK_DATA = false
    }

    suspend fun getAllDrains(): List<Drain> {
        if (USE_MOCK_DATA) {
            delay(300) // pretend it's a real network call, so loading spinners still make sense
            return mockDrains
        }
        return api.getAllDrains()
    }

    suspend fun getDrainDetail(id: Int): DrainDetail {
        if (USE_MOCK_DATA) {
            delay(300)
            return mockDrainDetails.first { it.id == id }
        }
        return api.getDrainDetail(id)
    }

    suspend fun getDrainHistory(id: Int): List<InspectionRecord> {
        if (USE_MOCK_DATA) {
            delay(300)
            return mockHistory
        }
        return api.getDrainHistory(id)
    }

    suspend fun getFloodRisk(lat: Double, lon: Double, blockageRatio: Double): FloodRiskResponse =
        api.getFloodRisk(FloodRiskRequest(lat, lon, blockageRatio)) // this one's real — already confirmed live

    // ---------------- Fake data below ----------------

    private val mockDrains = listOf(
        Drain(1, 18.6298, 73.7997, "Pimpri Chowk", "CRITICAL", 0.82, "2026-07-18"),
        Drain(2, 18.6350, 73.8050, "Chinchwad Station Rd", "ELEVATED", 0.55, "2026-07-15"),
        Drain(3, 18.6410, 73.7920, "Nigdi Sector 26", "LOW", 0.20, "2026-07-20"),
        Drain(4, 18.6250, 73.8100, "Akurdi Market", "CRITICAL", 0.91, "2026-07-10"),
        Drain(5, 18.6480, 73.7850, "Dapodi Bridge", "LOW", 0.12, "2026-07-22")
    )

    private val mockDrainDetails = listOf(
        DrainDetail(1, 18.6298, 73.7997, "Pimpri Chowk", "CRITICAL", 0.82, "2026-07-18",
            14.2, 78, 27.5, "Immediate dispatch: High probability of localized structural overflow.", "2026-06-01"),
        DrainDetail(2, 18.6350, 73.8050, "Chinchwad Station Rd", "ELEVATED", 0.55, "2026-07-15",
            6.5, 65, 29.0, "Schedule cleaning within 48 hours.", "2026-05-20"),
        DrainDetail(3, 18.6410, 73.7920, "Nigdi Sector 26", "LOW", 0.20, "2026-07-20",
            1.0, 55, 31.0, "No action needed.", "2026-07-01"),
        DrainDetail(4, 18.6250, 73.8100, "Akurdi Market", "CRITICAL", 0.91, "2026-07-10",
            18.9, 82, 26.0, "Immediate dispatch: High probability of localized structural overflow.", "2026-04-15"),
        DrainDetail(5, 18.6480, 73.7850, "Dapodi Bridge", "LOW", 0.12, "2026-07-22",
            0.5, 50, 30.5, "No action needed.", "2026-07-05")
    )

    private val mockHistory = listOf(
        InspectionRecord("2026-07-18", 0.82, "CRITICAL", "Pending Cleanup"),
        InspectionRecord("2026-06-20", 0.60, "ELEVATED", "Cleaned"),
        InspectionRecord("2026-05-15", 0.45, "ELEVATED", "Cleaned"),
        InspectionRecord("2026-04-01", 0.30, "LOW", "Cleaned")
    )
}