package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HistoricalDataResponse(
    @Json(name = "error")
    val error: Any? = Any(),
    @Json(name = "items")
    val items: Map<Int,MapValue> = mapOf(),
    @Json(name = "meta")
    val meta: Meta = Meta()
) {
    @JsonClass(generateAdapter = true)
    data class MapValue(
        @Json(name = "close")
        val close: Double = 0.0,
        @Json(name = "date")
        val date: String = "",
        @Json(name = "high")
        val high: Double = 0.0,
        @Json(name = "low")
        val low: Double = 0.0,
        @Json(name = "open")
        val `open`: Double = 0.0
    )

    @JsonClass(generateAdapter = true)
    data class Meta(
        @Json(name = "chartPreviousClose")
        val chartPreviousClose: Double = 0.0,
        @Json(name = "currency")
        val currency: String = "",
        @Json(name = "dataGranularity")
        val dataGranularity: String = "",
        @Json(name = "exchangeName")
        val exchangeName: String = "",
        @Json(name = "exchangeTimezoneName")
        val exchangeTimezoneName: String = "",
        @Json(name = "firstTradeDate")
        val firstTradeDate: Int = 0,
        @Json(name = "gmtoffset")
        val gmtoffset: Int = 0,
        @Json(name = "instrumentType")
        val instrumentType: String = "",
        @Json(name = "previousClose")
        val previousClose: Double = 0.0,
        @Json(name = "priceHint")
        val priceHint: Int = 0,
        @Json(name = "range")
        val range: String = "",
        @Json(name = "regularMarketPrice")
        val regularMarketPrice: Double = 0.0,
        @Json(name = "regularMarketTime")
        val regularMarketTime: Int = 0,
        @Json(name = "scale")
        val scale: Int = 0,
        @Json(name = "symbol")
        val symbol: String = "",
        @Json(name = "timezone")
        val timezone: String = ""
    )
}