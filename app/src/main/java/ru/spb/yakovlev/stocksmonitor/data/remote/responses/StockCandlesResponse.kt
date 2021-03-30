package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import java.util.*

data class StockCandlesResponse(
    @Json(name = "c")
    val closePrices: List<Double> = listOf(),
    @Json(name = "h")
    val highPrices: List<Double> = listOf(),
    @Json(name = "l")
    val lowPrices: List<Double> = listOf(),
    @Json(name = "o")
    val openPrices: List<Double> = listOf(),
    @Json(name = "s")
    val status: String = "",
    @Json(name = "t")
    val timestamp: List<Date> = listOf(),
    @Json(name = "v")
    val volume: List<Int> = listOf()
)