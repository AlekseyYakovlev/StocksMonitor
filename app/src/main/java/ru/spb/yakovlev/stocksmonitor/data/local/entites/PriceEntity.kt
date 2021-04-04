package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PriceEntity (
    @PrimaryKey
    val ticker: String,
    val currentPrice: Double = 0.0,
    val previousClosePrice: Double =0.0,
    val lastUpdated: Date = Date(),
)