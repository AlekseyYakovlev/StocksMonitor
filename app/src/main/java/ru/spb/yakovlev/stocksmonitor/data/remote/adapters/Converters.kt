package ru.spb.yakovlev.stocksmonitor.data.remote.adapters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateAdapter {
    @FromJson
    fun fromJson(timestamp: Int) = Date(timestamp*1000L)

    @ToJson
    fun toJson(date: Date) = ((date.time)/1000).toInt()
}

class ListConverter {
    companion object {
        const val SPLITTING_SYMBOLS = ";"
    }

    @TypeConverter
    fun String?.toListOfStrings(): List<String> =
        this?.split(SPLITTING_SYMBOLS) ?: emptyList()

    @TypeConverter
    fun List<String>.toStringConverter(): String =
        this.joinToString(separator = SPLITTING_SYMBOLS) { it }
}