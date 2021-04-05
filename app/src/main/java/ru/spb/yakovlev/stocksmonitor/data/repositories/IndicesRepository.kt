package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.spb.yakovlev.stocksmonitor.data.local.daos.IndexDao
import ru.spb.yakovlev.stocksmonitor.data.local.entites.IndexEntry
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.IndexResponse
import ru.spb.yakovlev.stocksmonitor.model.Index
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndicesRepository @Inject constructor(
    private val indexDao: IndexDao,
    private val finHubRest: FinHubRestService,
) {

    fun getIndexFlow(symbol: String): Flow<Index> =
        indexDao.findIndexBySymbolFlow(symbol)
            .onEach { priceEntry ->
                if (priceEntry == null) {
                    updateIndex(symbol)
                }
            }
            .filterNotNull()
            .map { it.toIndex() }

    suspend fun updateIndex(symbol: String) {
        loadIndexFromNet(symbol)?.let {
            indexDao.insert(it.toIndexEntry())
        }
    }

    private suspend fun loadIndexFromNet(indexName: String): IndexResponse? =
        try {
            finHubRest.loadIndices(indexName)
        } catch (e: Exception) {
            Timber.tag("IndicesRepository").e(e)
            null
        }
}

private fun IndexResponse.toIndexEntry() = IndexEntry(
    symbol = symbol,
    constituents = constituents,
    name = getIndexNameBySymbol(symbol),
    lastUpdated = Date()
)

private fun IndexEntry.toIndex() = Index(
    symbol = symbol,
    constituents = constituents,
    name = getIndexNameBySymbol(symbol)
)

/**
 * Supported indices are: ^GSPC (S&P 500), ^NDX (Nasdaq 100), ^DJI (Dow Jones)
 */
private fun getIndexNameBySymbol(symbol: String): String =
    when (symbol) {
        "^GSPC" -> "S&P 500"
        "^NDX" -> " Nasdaq 100"
        "^DJI" -> "Dow Jones"
        else -> "Unknown"
    }

