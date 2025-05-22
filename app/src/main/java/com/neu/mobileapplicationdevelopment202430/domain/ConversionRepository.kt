package com.neu.mobileapplicationdevelopment202430.domain

interface ConversionRepository {
    fun getConversionFactor(unit: UnitType): Double?
}
