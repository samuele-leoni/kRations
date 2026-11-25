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

    override fun schedule(items: List<LowStockItem>, reminderDate: LocalDateTime) {
        alarmeeService.local.schedule(
            alarmee = Alarmee(
                uuid = DIGEST_UUID,
                notificationTitle = "Low stock",
                notificationBody = digestBody(items),
                scheduledDateTime = reminderDate,
                androidNotificationConfiguration = AndroidNotificationConfiguration(
                    priority = AndroidNotificationPriority.DEFAULT,
                    channelId = LOW_STOCK_CHANNEL_ID,
                ),
                iosNotificationConfiguration = IosNotificationConfiguration(),
            )
        )
    }

    override fun cancel() {
        alarmeeService.local.cancel(uuid = DIGEST_UUID)
    }

    private fun digestBody(items: List<LowStockItem>): String {
        val listed = items.take(MAX_LISTED_ITEMS).joinToString(separator = ", ") { item ->
            "${item.productName} (${item.stock.formatQuantity()}/${item.minQuantity.formatQuantity()} ${item.unit})"
        }
        val hidden = items.size - MAX_LISTED_ITEMS
        return if (hidden > 0) "$listed and $hidden more" else listed
    }

    private companion object {
        const val DIGEST_UUID = "low-stock-digest"
        const val MAX_LISTED_ITEMS = 4
    }
}
