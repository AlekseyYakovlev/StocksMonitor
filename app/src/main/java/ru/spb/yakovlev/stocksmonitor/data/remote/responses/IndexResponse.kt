package ru.spb.yakovlev.stocksmonitor.data.remote.responses
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IndexResponse(
    @Json(name = "constituents")
    val constituents: List<String> = listOf(),
    @Json(name = "symbol")
    val symbol: String = ""
)