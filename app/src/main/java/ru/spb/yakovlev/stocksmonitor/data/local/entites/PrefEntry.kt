package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class PrefEntry(
    @PrimaryKey
    val name: String = "",
    val value: String = "",
    val listValue: List<String> = emptyList()
)