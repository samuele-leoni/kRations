package com.example.krations.core.ui.utils

internal fun Double.formatQuantity(): String =
    if (this % 1.0 == 0.0) toLong().toString() else toString()