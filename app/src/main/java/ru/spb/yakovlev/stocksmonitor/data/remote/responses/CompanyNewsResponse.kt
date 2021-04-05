package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyNewsResponse(
    @Json(name = "category")
    val category: String = "",
    @Json(name = "datetime")
    val datetime: Long = 0,
    @Json(name = "headline")
    val headline: String = "",
    @Json(name = "id")
    val id: String = "",
    @Json(name = "image")
    val image: String = "",
    @Json(name = "related")
    val related: String = "",
    @Json(name = "source")
    val source: String = "",
    @Json(name = "summary")
    val summary: String = "",
    @Json(name = "url")
    val url: String = ""
)