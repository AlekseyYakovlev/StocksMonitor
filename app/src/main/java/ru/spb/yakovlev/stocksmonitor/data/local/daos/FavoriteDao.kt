package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.entites.FavoriteEntry

@Dao
interface FavoriteDao : BaseDao<FavoriteEntry> {
    @Query(
        """
            SELECT ticker 
            FROM FavoriteEntry
        """
    )
    fun getFavoritesFlow(): Flow<List<String>>
}