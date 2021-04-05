package ru.spb.yakovlev.stocksmonitor.model

import java.util.*

data class News(
    val id: String = "0",
    val category: String = "",
    val datetime: Date = Date(),
    val headline: String = "",
    val image: String = "",
    val related: String = "", //ticker
    val source: String = "",
    val summary: String = "",
    val url: String = "",
)