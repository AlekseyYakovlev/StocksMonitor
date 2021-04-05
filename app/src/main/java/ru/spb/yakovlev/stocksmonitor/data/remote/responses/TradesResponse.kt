package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TradesResponse(
    @Json(name = "data")
    val data: List<Data>? = listOf(),
    @Json(name = "type")
    val type: String = ""
) {
    data class Data(
        @Json(name = "c")
        val conditions: List<String> = listOf(),
        @Json(name = "p")
        val price: Double = 0.0,
        @Json(name = "s")
        val symbol: String = "",
        @Json(name = "t")
        val timestamp: Long = 0,
        @Json(name = "v")
        val volume: Int = 0
    )
}