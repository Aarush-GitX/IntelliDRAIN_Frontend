package com.mastermind.intellidrain.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.intellidrain.data.model.Drain
import com.mastermind.intellidrain.data.repository.DrainRepository
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val repository = DrainRepository()

    private val _drains = MutableLiveData<List<Drain>>()
    val drains: LiveData<List<Drain>> = _drains

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadDrains() {
        viewModelScope.launch {
            try {
                _drains.value = repository.getAllDrains()
            } catch (e: Exception) {
                _error.value = "Couldn't load drains: ${e.message}"
            }
        }
    }
}