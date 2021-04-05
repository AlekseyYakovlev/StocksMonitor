package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// In day prices
@JsonClass(generateAdapter = true)
data class QuotesResponse(
    @Json(name = "c")
    val currentPrice: Double = 0.0,
    @Json(name = "h")
    val highestPrice: Double = 0.0,
    @Json(name = "l")
    val lowestPrice: Double = 0.0,
    @Json(name = "o")
    val openPrice: Double = 0.0,
    @Json(name = "pc")
    val previousClosePrice: Double = 0.0,
    @Json(name = "t")
    val timestamp: Long = 0
)