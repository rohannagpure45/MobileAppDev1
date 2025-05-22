package com.neu.mobileapplicationdevelopment202430.domain

class ConvertUnitsUseCase(private val repository: ConversionRepository) {
    /**
     * Convert a value from one unit to another.
     * @param value The numeric value to convert.
     * @param fromUnit The unit of the input value.
     * @param toUnit   The target unit for conversion.
     * @return The converted value, or null if conversion is not possible (e.g., units of different categories).
     */
    fun execute(value: Double, fromUnit: UnitType, toUnit: UnitType): Double? {
        // Only allow conversion within the same category (e.g., weight to weight)
        if (fromUnit.category != toUnit.category) {
            return null  // incompatible units
        }
        val fromFactor = repository.getConversionFactor(fromUnit) ?: return null
        val toFactor = repository.getConversionFactor(toUnit) ?: return null
        // Convert: first to base unit, then to target unit
        val baseValue = value * fromFactor    // convert input to base unit (Kg, Litre, or Meter)
        val resultValue = baseValue / toFactor // convert base unit value to target unit
        return resultValue
    }
}
