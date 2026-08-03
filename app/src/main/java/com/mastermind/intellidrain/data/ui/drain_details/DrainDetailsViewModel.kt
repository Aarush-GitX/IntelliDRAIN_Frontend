package com.mastermind.intellidrain.ui.drain_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermind.intellidrain.data.model.DrainDetail
import com.mastermind.intellidrain.data.repository.DrainRepository
import kotlinx.coroutines.launch

class DrainDetailsViewModel : ViewModel() {

    private val repository = DrainRepository()

    private val _drain = MutableLiveData<DrainDetail>()
    val drain: LiveData<DrainDetail> = _drain

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadDrain(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _drain.value = repository.getDrainDetail(id)
            } catch (e: Exception) {
                _error.value = "Couldn't load drain details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}