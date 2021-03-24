package ru.spb.yakovlev.stocksmonitor.model

data class Stock(
    val ticker: String = "",
    val compName: String = "",
    val price: Float = 0f,
    val currency: String = "",
    val delta: Float = 0f,
    val logo: String = "",
)