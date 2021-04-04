package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.entites.IndexEntry

@Dao
interface IndexDao : BaseDao<IndexEntry> {
    @Query(
        """
            SELECT * 
            FROM IndexEntry
            WHERE symbol = :symbol            
            LIMIT 1
        """
    )
    fun findIndexBySymbolFlow(symbol: String): Flow<IndexEntry?>
}