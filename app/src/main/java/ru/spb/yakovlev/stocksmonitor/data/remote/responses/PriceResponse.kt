package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json

data class PriceResponse(
    @Json(name = "data")
    val `data`: List<Data> = listOf(),
    @Json(name = "type")
    val type: String = ""
) {
    data class Data(
        @Json(name = "c")
        val c: List<String> = listOf(),
        @Json(name = "p")
        val p: Double = 0.0,
        @Json(name = "s")
        val s: String = "",
        @Json(name = "t")
        val t: Long = 0,
        @Json(name = "v")
        val v: Int = 0
    )
}