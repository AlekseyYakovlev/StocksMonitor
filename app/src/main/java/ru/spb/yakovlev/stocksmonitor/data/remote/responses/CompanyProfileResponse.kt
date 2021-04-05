package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyProfileResponse(
    @Json(name = "country")
    val country: String = "",
    @Json(name = "currency")
    val currency: String = "",
    @Json(name = "exchange")
    val exchange: String = "",
    @Json(name = "finnhubIndustry")
    val industry: String = "",
    @Json(name = "ipo")
    val ipo: String = "",
    @Json(name = "logo")
    val logo: String = "",
    @Json(name = "marketCapitalization")
    val marketCapitalization: Double = 0.0,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "phone")
    val phone: String = "",
    @Json(name = "shareOutstanding")
    val shareOutstanding: Double = 0.0,
    @Json(name = "ticker")
    val ticker: String = "",
    @Json(name = "weburl")
    val webUrl: String = ""
)