package com.mastermind.intellidrain.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.intellidrain.data.model.Drain
import com.mastermind.intellidrain.data.repository.DrainRepository
import kotlinx.coroutines.launch

data class HomeStats(
    val totalDrains: Int,
    val criticalDrains: Int,
    val highRiskAreas: Int,
    val pendingInspections: Int
)

class HomeViewModel : ViewModel() {

    private val repository = DrainRepository()

    private val _stats = MutableLiveData<HomeStats>()
    val stats: LiveData<HomeStats> = _stats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val drains: List<Drain> = repository.getAllDrains()
                _stats.value = HomeStats(
                    totalDrains = drains.size,
                    criticalDrains = drains.count { it.risk_level == "CRITICAL" },
                    highRiskAreas = drains.count { it.risk_level == "CRITICAL" || it.risk_level == "ELEVATED" },
                    pendingInspections = drains.count { it.blockage_ratio > 0.5 } // placeholder rule until backend gives real status
                )
            } catch (e: Exception) {
                _error.value = "Couldn't load dashboard: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}