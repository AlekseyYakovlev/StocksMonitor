package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// ^GSPC (S&P 500), ^NDX (Nasdaq 100), ^DJI (Dow Jones)
@Entity
class IndexEntry(
    @PrimaryKey
    val symbol: String = "",
    val constituents: List<String> = emptyList(),
    val name: String = "",
    val lastUpdated: Date = Date()
)