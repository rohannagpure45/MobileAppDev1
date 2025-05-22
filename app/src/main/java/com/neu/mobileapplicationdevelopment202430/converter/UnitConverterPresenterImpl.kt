package com.neu.mobileapplicationdevelopment202430.converter

import com.neu.mobileapplicationdevelopment202430.domain.ConvertUnitsUseCase
import com.neu.mobileapplicationdevelopment202430.domain.UnitType

class UnitConverterPresenterImpl(
    private val convertUnitsUseCase: ConvertUnitsUseCase
) : UnitConverterPresenter {

    private var view: UnitConverterView? = null

    override fun attach(view: UnitConverterView) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }

    override fun onConvertClicked(inputValue: String, fromUnit: UnitType, toUnit: UnitType) {
        if (inputValue.isBlank()) {
            view?.showError("Please enter a value")
            return
        }
        // Try to parse the input string to a number
        val value = inputValue.toDoubleOrNull()
        if (value == null) {
            view?.showError("Invalid number format")
            return
        }
        // Perform conversion via the use-case
        val result = convertUnitsUseCase.execute(value, fromUnit, toUnit)
        if (result == null) {
            // Units are incompatible (different categories)
            view?.showError("Cannot convert ${fromUnit.displayName} to ${toUnit.displayName}")
        } else {
            // Format the result (e.g., 4 decimal places) and display it
            val resultText = String.format("%.4f", result)
            view?.showResult(resultText)
        }
    }
}
