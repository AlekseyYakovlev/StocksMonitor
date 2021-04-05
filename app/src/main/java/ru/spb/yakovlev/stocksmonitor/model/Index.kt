package ru.spb.yakovlev.stocksmonitor.model

data class Index(
    val symbol: String = "",
    val name: String = "",
    val constituents: List<String> = emptyList(),
)