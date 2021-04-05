package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NewsEntity(
    @PrimaryKey
    val id: String = "",
    val category: String = "",
    val datetime: Date = Date(),
    val headline: String = "",
    val image: String = "",
    val related: String = "", //ticker
    val source: String = "",
    val summary: String = "",
    val url: String = "",
)