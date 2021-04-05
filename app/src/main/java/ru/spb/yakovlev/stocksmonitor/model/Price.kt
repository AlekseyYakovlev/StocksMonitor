package ru.spb.yakovlev.stocksmonitor.model

import java.util.*

data class Price(
    val ticker: String,
    val currentPrice: Double = 0.0,
    val previousClosePrice: Double = 0.0,
    val delta: Double = currentPrice - previousClosePrice,
    val deltaPercent: Double = delta / previousClosePrice,
    val lastUpdated: Date = Date(),
)