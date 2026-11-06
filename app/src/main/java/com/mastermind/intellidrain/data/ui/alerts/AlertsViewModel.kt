package com.mastermind.intellidrain.ui.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.intellidrain.data.model.Alert
import com.mastermind.intellidrain.data.repository.DrainRepository
import kotlinx.coroutines.launch

class AlertsViewModel : ViewModel() {

    private val repository = DrainRepository()

    private val _alerts = MutableLiveData<List<Alert>>()
    val alerts: LiveData<List<Alert>> = _alerts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadAlerts() {
        viewModelScope.launch {
            _isLoading.value = true
            val drains = repository.getAllDrains()

            _alerts.value = drains
                .filter { it.risk_level == "CRITICAL" || it.risk_level == "ELEVATED" }
                .map { drain ->
                    Alert(
                        drainId = drain.id,
                        title = if (drain.risk_level == "CRITICAL") "Critical Drain Alert" else "Overflow Risk Warning",
                        priority = drain.risk_level,
                        location = drain.area_name,
                        time = "Recently",
                        recommendedAction = if (drain.risk_level == "CRITICAL")
                            "Immediate cleaning required" else "Schedule inspection soon"
                    )
                }

                .sortedByDescending { it.priority == "CRITICAL" } // criticals show first

            _isLoading.value = false
        }
    }
}