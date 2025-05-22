package com.neu.mobileapplicationdevelopment202430.converter

import com.neu.mobileapplicationdevelopment202430.domain.UnitType

// MVP Contract defining View and Presenter roles
interface UnitConverterView {
    fun showResult(result: String)
    fun showError(message: String)
}

interface UnitConverterPresenter {
    fun attach(view: UnitConverterView)
    fun detach()
    fun onConvertClicked(inputValue: String, fromUnit: UnitType, toUnit: UnitType)
}
