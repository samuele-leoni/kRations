package com.example.krations.alerts.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.krations.alerts.domain.LowStockItemsRepository
import com.example.krations.alerts.domain.RescheduleLowStockRemindersUseCase
import kotlin.time.Clock.System.now
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject

@Composable
fun LowStockRemindersEffect() {
    val lowStockItemsRepository = koinInject<LowStockItemsRepository>()
    val rescheduleLowStockReminders = koinInject<RescheduleLowStockRemindersUseCase>()

    LaunchedEffect(Unit) {
        lowStockItemsRepository.observeItems().collect { items ->
            rescheduleLowStockReminders(items, now().toLocalDateTime(TimeZone.currentSystemDefault()))
        }
    }
}
