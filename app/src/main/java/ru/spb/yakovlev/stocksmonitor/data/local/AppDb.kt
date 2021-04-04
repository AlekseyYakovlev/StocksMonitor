package ru.spb.yakovlev.stocksmonitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.spb.yakovlev.stocksmonitor.BuildConfig
import ru.spb.yakovlev.stocksmonitor.data.local.converters.DateConverter
import ru.spb.yakovlev.stocksmonitor.data.local.daos.*
import ru.spb.yakovlev.stocksmonitor.data.local.entites.*
import ru.spb.yakovlev.stocksmonitor.data.remote.adapters.ListConverter

@Database(
    entities = [
        CompanyEntity::class,
        PriceEntity::class,
        NewsEntity::class,
        IndexEntry::class,
        FavoriteEntry::class,
        PrefEntry::class,
    ],
    version = AppDb.DATABASE_VERSION,
    exportSchema = false,
)
@TypeConverters(
    DateConverter::class,
    ListConverter::class,
)
abstract class AppDb : RoomDatabase() {

    abstract fun priceDao(): PriceDao
    abstract fun companyDao(): CompanyDao
    abstract fun newsDao(): NewsDao
    abstract fun indexDao(): IndexDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun prefsDao(): PrefsDao

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }
}