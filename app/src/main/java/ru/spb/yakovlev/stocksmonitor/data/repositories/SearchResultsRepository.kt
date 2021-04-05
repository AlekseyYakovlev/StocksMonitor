package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.local.daos.PrefsDao
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import timber.log.Timber
import java.util.*
import javax.inject.Inject

interface ISearchResultsRepository {
    suspend fun findTickers(query: String): List<String>
}

class SearchResultsRepository @Inject constructor(
    private val finHubRest: FinHubRestService,
    private val prefsDao: PrefsDao,
) : ISearchResultsRepository {

    override suspend fun findTickers(query: String): List<String> =
        loadFromNet(query)

    private suspend fun loadFromNet(query: String): List<String> =
        try {
            val res = finHubRest.search(query)
            if (res.resCount > 0) res.result?.map { it.symbol } ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            Timber.tag("SearchResultsRepository").e(e)
            emptyList()
        }

    val resentQueriesFlow: Flow<List<String>> =
        prefsDao.getListValueFlow(RESENT_QUERIES).map { it }

    suspend fun updateResentQueries(newQuery: String) {
        val dbVal= prefsDao.getListValue(RESENT_QUERIES)
        Timber.tag("1234567 ").d(dbVal.size.toString())
        val resentQueries = LinkedList<String>(prefsDao.getListValue(RESENT_QUERIES))
        Timber.tag("1234567 ").d(resentQueries.size.toString())
        resentQueries.remove(newQuery)
        resentQueries.addFirst(newQuery)
        if (resentQueries.size > 20) resentQueries.pollLast()
        prefsDao.saveList(RESENT_QUERIES, resentQueries)
    }

    val popularQueries = listOf(
        "CAT",
        "IBM",
        "HD",
        "WBA",
        "AAPL",
        "VZ",
        "MMM",
        "BA",
        "MSFT",
        "JPM",
        "MRK",
        "TRV",
        "KO",
        "MCD",
        "PG",
        "JNJ",
        "INTC",
        "CSCO",
        "UNH",
        "DOW",
        "DIS",
        "CRM",
        "GS",
        "WMT",
        "NKE",
        "V",
        "AXP",
        "CVX",
        "HON",
        "AMGN"
    )
}

private const val RESENT_QUERIES = "RESENT_QUERIES"