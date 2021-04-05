package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.entites.CompanyEntity

@Dao
interface CompanyDao : BaseDao<CompanyEntity> {
    @Query(
        """
            SELECT * 
            FROM CompanyEntity
            WHERE ticker = :ticker            
            LIMIT 1
        """
    )
    fun findCompanyByTicker(ticker: String): CompanyEntity?

    @Query(
        """
            SELECT * 
            FROM CompanyEntity
            WHERE ticker = :ticker            
            LIMIT 1
        """
    )
    fun findCompanyByTickerFlow(ticker: String): Flow<CompanyEntity?>

    @Query(
        """
            SELECT * 
            FROM CompanyEntity
            WHERE ticker IN (:ticker)            
        """
    )
    fun findCompaniesByTickers(ticker: List<String>): List<CompanyEntity>
}