package com.example.krations.alerts.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

object ExpirationRules {
    val REMINDER_TIME = LocalTime(9, 0)

    fun reminderDate(expiration: LocalDate, expiryAlertDays: Int): LocalDateTime =
        LocalDateTime(expiration.minus(expiryAlertDays, DateTimeUnit.DAY), REMINDER_TIME)

    // TODO to be used in the home screen to filter the list
    fun isExpiringSoon(expiration: LocalDate, today: LocalDate, expiryAlertDays: Int): Boolean =
        expiration >= today && expiration <= today.plus(expiryAlertDays, DateTimeUnit.DAY)

    fun reminderDate(expiration: LocalDate, now: LocalDateTime, expiryAlertDays: Int): LocalDateTime? =
        if (expiration < now.date) null
        else reminderDate(expiration, expiryAlertDays).takeIf { it > now }
            ?: nextReminderSlot(now).takeIf { it.date <= expiration }

    private fun nextReminderSlot(now: LocalDateTime): LocalDateTime =
        LocalDateTime(now.date, REMINDER_TIME).takeIf { it > now }
            ?: LocalDateTime(now.date.plus(1, DateTimeUnit.DAY), REMINDER_TIME)
}