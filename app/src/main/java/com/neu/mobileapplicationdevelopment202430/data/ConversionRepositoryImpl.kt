package com.neu.mobileapplicationdevelopment202430.data

import com.neu.mobileapplicationdevelopment202430.domain.ConversionRepository
import com.neu.mobileapplicationdevelopment202430.domain.UnitType

class ConversionRepositoryImpl : ConversionRepository {
    // Conversion factors to base units (base units: 1 Kg, 1 Litre, 1 Meter)
    private val conversionToBase: Map<UnitType, Double> = mapOf(
        UnitType.KG to 1.0,
        UnitType.POUND to 0.453592,   // 1 Pound = 0.453592 Kg
        UnitType.LITER to 1.0,
        UnitType.GALLON to 3.78541,   // 1 Gallon = 3.78541 Litre
        UnitType.METER to 1.0,
        UnitType.YARD to 0.9144      // 1 Yard = 0.9144 Meter
    )

    override fun getConversionFactor(unit: UnitType): Double? {
        return conversionToBase[unit]
    }
}
