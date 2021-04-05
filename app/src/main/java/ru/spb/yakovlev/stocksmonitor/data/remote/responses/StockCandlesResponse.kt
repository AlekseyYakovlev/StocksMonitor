package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

// History data
@JsonClass(generateAdapter = true)
data class StockCandlesResponse(
    @Json(name = "c")
    val closePricesList: List<Double> = listOf(),
    @Json(name = "h")
    val highPricesList: List<Double> = listOf(),
    @Json(name = "l")
    val lowPricesList: List<Double> = listOf(),
    @Json(name = "o")
    val openPricesList: List<Double> = listOf(),
    @Json(name = "s")
    val status: String = "",
    @Json(name = "t")
    val timestampList: List<Long> = listOf(),
    @Json(name = "v")
    val volumeList: List<Int> = listOf()
)