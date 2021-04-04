package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
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
            WHERE ticker IN (:ticker)            
        """
    )
    fun findCompaniesByTickers(ticker: List<String>): List<CompanyEntity>
}