package com.neu.mobileapplicationdevelopment202430.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.data.ConversionRepositoryImpl
import com.neu.mobileapplicationdevelopment202430.domain.ConvertUnitsUseCase
import com.neu.mobileapplicationdevelopment202430.domain.UnitType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Sealed class representing UI state for conversion result
sealed class ConversionUiState {
    data class Success(val resultText: String) : ConversionUiState()
    data class Error(val errorText: String) : ConversionUiState()
}

class ConverterViewModel : ViewModel() {
    private val convertUnitsUseCase = ConvertUnitsUseCase(ConversionRepositoryImpl())

    // LiveData holding the current conversion result or error state
    private val _conversionResult = MutableLiveData<ConversionUiState>()
    val conversionResult: LiveData<ConversionUiState> = _conversionResult

    fun convert(inputValue: String, fromUnit: UnitType, toUnit: UnitType) {
        if (inputValue.isBlank()) {
            _conversionResult.value = ConversionUiState.Error("Please enter a value")
            return
        }
        // Try to parse the input string to a Double
        val value = inputValue.toDoubleOrNull()
        if (value == null) {
            _conversionResult.value = ConversionUiState.Error("Invalid number format")
            return
        }
        // Perform the conversion on a background thread
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                convertUnitsUseCase.execute(value, fromUnit, toUnit)
            }
            if (result == null) {
                // Post error state (incompatible units)
                _conversionResult.value = ConversionUiState.Error("Cannot convert ${fromUnit.displayName} to ${toUnit.displayName}")
            } else {
                // Format result and post success state
                val resultText = String.format("%.4f", result)
                _conversionResult.value = ConversionUiState.Success(resultText)
            }
        }
    }
}
