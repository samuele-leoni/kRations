package com.example.krations.alerts.data

import com.example.krations.alerts.domain.LOW_STOCK_CHANNEL_ID
import com.example.krations.alerts.domain.LowStockNotifier
import com.example.krations.alerts.domain.model.LowStockItem
import com.example.krations.core.ui.utils.formatQuantity
import com.tweener.alarmee.AlarmeeService
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.AndroidNotificationPriority
import com.tweener.alarmee.model.IosNotificationConfiguration
import kotlinx.datetime.LocalDateTime

class AlarmeeLowStockNotifier(
    private val alarmeeService: AlarmeeService,
) : LowStockNotifier {

    override fun schedule(item: LowStockItem, reminderDate: LocalDateTime) {
        alarmeeService.local.schedule(
            alarmee = Alarmee(
                uuid = item.productId,
                notificationTitle = "Low stock",
                notificationBody = "${item.productName}: ${item.stock.formatQuantity()} ${item.unit} left, minimum is ${item.minQuantity.formatQuantity()}",
                scheduledDateTime = reminderDate,
                androidNotificationConfiguration = AndroidNotificationConfiguration(
                    priority = AndroidNotificationPriority.DEFAULT,
                    channelId = LOW_STOCK_CHANNEL_ID,
                ),
                iosNotificationConfiguration = IosNotificationConfiguration(),
            )
        )
    }

    override fun cancel(productId: String) {
        alarmeeService.local.cancel(uuid = productId)
    }
}
