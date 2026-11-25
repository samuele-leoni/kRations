package com.example.krations.alerts.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

object LowStockRules {

    fun reminderDate(now: LocalDateTime): LocalDateTime {
        val todaySlot = LocalDateTime(now.date, ExpirationRules.REMINDER_TIME)
        return if (todaySlot > now) todaySlot
        else LocalDateTime(now.date.plus(1, DateTimeUnit.DAY), ExpirationRules.REMINDER_TIME)
    }
}
