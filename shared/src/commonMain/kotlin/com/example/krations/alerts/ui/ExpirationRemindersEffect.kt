package com.example.krations.alerts.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.krations.alerts.domain.AddExpiringItemsToShoppingListUseCase
import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.RescheduleExpirationRemindersUseCase
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Notification
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.isGranted
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.Clock.System.now

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExpirationRemindersEffect() {
    val expiringItemsRepository = koinInject<ExpiringItemsRepository>()
    val rescheduleExpirationReminders = koinInject<RescheduleExpirationRemindersUseCase>()
    val notificationPermission = rememberPermissionState(Permission.Notification)
    val addExpiringItems = koinInject<AddExpiringItemsToShoppingListUseCase>()

    LaunchedEffect(Unit) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }

        var previousBatchIds = emptySet<String>()
        expiringItemsRepository.observeItems().collect { items ->
            val currentBatchIds = items.map { it.batchId }.toSet()
            rescheduleExpirationReminders(
                items,
                now().toLocalDateTime(TimeZone.currentSystemDefault()),
                removedBatchIds = previousBatchIds - currentBatchIds,
            )
            previousBatchIds = currentBatchIds
            addExpiringItems(items)
        }
    }
}
