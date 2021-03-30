package ru.spb.yakovlev.stocksmonitor.data.remote.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateAdapter {
    @FromJson
    fun fromJson(timestamp: Int) = Date(timestamp*1000L)

    @ToJson
    fun toJson(date: Date) = ((date.time)/1000).toInt()
}