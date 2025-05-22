package com.neu.mobileapplicationdevelopment202430.domain

enum class UnitCategory {
    WEIGHT, VOLUME, LENGTH
}

enum class UnitType(val category: UnitCategory, val displayName: String) {
    KG(UnitCategory.WEIGHT, "Kg"),
    POUND(UnitCategory.WEIGHT, "Pound"),
    LITER(UnitCategory.VOLUME, "Litre"),
    GALLON(UnitCategory.VOLUME, "Gallon"),
    METER(UnitCategory.LENGTH, "Meter"),
    YARD(UnitCategory.LENGTH, "Yard");

    override fun toString(): String {
        return displayName 
    }
}
