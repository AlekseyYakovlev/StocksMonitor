package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.entites.NewsEntity

@Dao
interface NewsDao : BaseDao<NewsEntity> {
    @Query(
        """
            SELECT * 
            FROM NewsEntity
            WHERE related = :ticker
        """
    )
    fun findNewsByTickerFlow(ticker: String): Flow<List<NewsEntity>>
}