package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavoriteEntry(
    @PrimaryKey
    val ticker: String = "",
)