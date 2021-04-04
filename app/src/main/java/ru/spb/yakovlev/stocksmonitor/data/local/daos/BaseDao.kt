package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.*

@Dao
interface BaseDao<T : Any> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Update
    suspend fun update(list: List<T>)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)
}