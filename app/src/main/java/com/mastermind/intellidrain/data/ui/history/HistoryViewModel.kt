package com.mastermind.intellidrain.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.intellidrain.data.model.InspectionRecord
import com.mastermind.intellidrain.data.repository.DrainRepository
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = DrainRepository()

    private val _history = MutableLiveData<List<InspectionRecord>>()
    val history: LiveData<List<InspectionRecord>> = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadHistory(drainId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _history.value = repository.getDrainHistory(drainId)
            _isLoading.value = false
        }
    }
}