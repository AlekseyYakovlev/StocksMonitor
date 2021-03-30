package ru.spb.yakovlev.stocksmonitor.data.remote.responses


import com.squareup.moshi.Json

data class CompanyProfileResponse(
    @Json(name = "country")
    val country: String = "",
    @Json(name = "currency")
    val currency: String = "",
    @Json(name = "exchange")
    val exchange: String = "",
    @Json(name = "finnhubIndustry")
    val finnhubIndustry: String = "",
    @Json(name = "ipo")
    val ipo: String = "",
    @Json(name = "logo")
    val logo: String = "",
    @Json(name = "marketCapitalization")
    val marketCapitalization: Int = 0,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "phone")
    val phone: String = "",
    @Json(name = "shareOutstanding")
    val shareOutstanding: Double = 0.0,
    @Json(name = "ticker")
    val ticker: String = "",
    @Json(name = "weburl")
    val weburl: String = ""
)