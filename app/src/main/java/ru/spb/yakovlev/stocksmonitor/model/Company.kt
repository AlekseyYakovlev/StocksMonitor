package ru.spb.yakovlev.stocksmonitor.model

import java.util.*

data class Company(
    val ticker: String = "",
    val country: String = "",
    val currency: String = "USD",
    val exchange: String = "",
    val ipo: String = "", //date
    val marketCapitalization: Double = 0.0,
    val name: String = "",
    val phone: String = "",
    val shareOutstanding: Double = 0.0,
    val webUrl: String = "",
    val logo: String = "",
    val industry: String = "",
    val lastUpdated: Date = Date(),
)