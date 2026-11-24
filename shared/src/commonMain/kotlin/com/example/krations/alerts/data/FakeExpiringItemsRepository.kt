package com.example.krations.alerts.data

import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.model.ExpiringItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class FakeExpiringItemsRepository : ExpiringItemsRepository {
    override fun observeItems() =
        with(Clock.System.todayIn(TimeZone.currentSystemDefault())) {
            flowOf(
                listOf(
                    ExpiringItem(
                        "fake-1",
                        "Pasta",
                        this.plus(3, DateTimeUnit.DAY)
                    ),
                    ExpiringItem(
                        "fake-2",
                        "Rice",
                        this.plus(10, DateTimeUnit.DAY)
                    ),
                    ExpiringItem(
                        "fake-3",
                        "Milk",
                        this.minus(1, DateTimeUnit.DAY)
                    ),
                    ExpiringItem(
                        "fake-4",
                        "Water",
                        this.plus(30, DateTimeUnit.DAY)
                    ),
                )
            )
        }
}