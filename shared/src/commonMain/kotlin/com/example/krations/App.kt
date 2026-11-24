package com.example.krations

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.RescheduleExpirationRemindersUseCase
import com.example.krations.shoppinglist.ui.ShoppingListScreen
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Notification
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.isGranted
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.Clock.System.now

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App() {
    val expiringItemsRepository = koinInject<ExpiringItemsRepository>()
    val rescheduleExpirationReminders = koinInject<RescheduleExpirationRemindersUseCase>()
    val notificationPermission = rememberPermissionState(Permission.Notification)

    LaunchedEffect(Unit) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }

        val now = now().toLocalDateTime(TimeZone.currentSystemDefault())
        rescheduleExpirationReminders(expiringItemsRepository.observeItems().first(), now)
    }

    MaterialTheme {
        Scaffold { innerPadding ->
            ShoppingListScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}