package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.entites.PriceEntity

@Dao
interface PriceDao : BaseDao<PriceEntity>{
    @Query(
        """
            SELECT * 
            FROM PriceEntity
            WHERE ticker = :ticker            
            LIMIT 1
        """
    )
    fun findPriceByTickerFlow(ticker: String): Flow<PriceEntity?>

    @Query(
        """
            SELECT * 
            FROM PriceEntity
            WHERE ticker = :ticker            
            LIMIT 1
        """
    )
    fun findPriceByTicker(ticker: String): PriceEntity?

    @Query(
        """
            UPDATE PriceEntity 
            SET currentPrice = :newPrice
            WHERE ticker = :ticker
        """
    )
    suspend fun updateLastPrice(ticker: String, newPrice:Double)
}