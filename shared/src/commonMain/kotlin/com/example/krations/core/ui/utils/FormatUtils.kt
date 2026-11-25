package com.example.krations.core.ui.utils

import kotlin.math.round

internal fun Double.formatQuantity(): String {
    val rounded = toTwoDecimals()
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else rounded.toString()
}

internal fun Double.toTwoDecimals(): Double = round(this * 100) / 100
