package com.example.unitconversion
import android.content.Context

class Length(val context: Context, val value: Float, val unit: String?) {
    public var value_meters: Float = 0.0F
    init {
        value_meters = when(unit) {
            "km" -> value * 1000F
            "cm" -> value / 100F
            "Inch" -> value / 39.37F
            "Feet" -> value / 3.281F
            else -> value
        }
    }

    public fun to_km(): Float {
        return value_meters / 1000F
    }

    public fun to_cm(): Float {
        return value_meters * 100F
    }

    public fun to_inches(): Float {
        return value_meters * 39.37F
    }

    public fun to_feet(): Float {
        return value_meters * 3.281F
    }
}