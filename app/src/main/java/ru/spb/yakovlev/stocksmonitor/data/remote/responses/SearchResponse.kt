package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "count")
    val resCount: Int = 0,
    @Json(name = "result")
    val result: List<Result>? = listOf()
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "description")
        val description: String? = "",
        @Json(name = "displaySymbol")
        val displaySymbol: String? = "",
        @Json(name = "primary")
        val primary: List<String>? = listOf(),
        @Json(name = "symbol")
        val symbol: String = "",
        @Json(name = "type")
        val type: String? = ""
    )
}