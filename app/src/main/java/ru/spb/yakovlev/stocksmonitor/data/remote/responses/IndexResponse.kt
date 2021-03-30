package ru.spb.yakovlev.stocksmonitor.data.remote.responses
import com.squareup.moshi.Json

data class IndexResponse(
    @Json(name = "constituents")
    val constituents: List<String> = listOf(),
    @Json(name = "symbol")
    val IndexName: String = ""
)