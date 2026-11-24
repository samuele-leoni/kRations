package com.example.krations.alerts.data

import com.example.krations.alerts.domain.EXPIRATION_CHANNEL_ID
import com.example.krations.alerts.domain.ExpirationNotifier
import com.example.krations.alerts.domain.model.ExpiringItem
import com.tweener.alarmee.AlarmeeService
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.AndroidNotificationPriority
import com.tweener.alarmee.model.IosNotificationConfiguration
import kotlinx.datetime.LocalDateTime

class AlarmeeExpirationNotifier(
    private val alarmeeService: AlarmeeService,
) : ExpirationNotifier {

    override fun schedule(item: ExpiringItem, expirationReminderDate: LocalDateTime) {
        alarmeeService.local.schedule(
            alarmee = Alarmee(
                // TODO understand whether to use the batchId or something else
                uuid = item.batchId,
                notificationTitle = "Expiring soon",
                notificationBody = "${item.productName} expires on ${item.expirationDate}",
                scheduledDateTime = expirationReminderDate,
                androidNotificationConfiguration = AndroidNotificationConfiguration(
                    priority = AndroidNotificationPriority.DEFAULT,
                    channelId = EXPIRATION_CHANNEL_ID,
                ),
                iosNotificationConfiguration = IosNotificationConfiguration(),
            )
        )
    }

    override fun cancel(batchId: String) {
        alarmeeService.local.cancel(uuid = batchId)
    }
}