package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.local.daos.NewsDao
import ru.spb.yakovlev.stocksmonitor.data.local.entites.NewsEntity
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.CompanyNewsResponse
import ru.spb.yakovlev.stocksmonitor.model.News
import java.util.*
import javax.inject.Inject


class NewsRepository @Inject constructor(
    private val finHubRest: FinHubRestService,
    private val newsDao: NewsDao,
) {
     fun getNewsForTicker(ticker: String): Flow<List<News>> =
       newsDao.findNewsByTickerFlow(ticker)
           .filterNotNull()
           .map { list ->
               list.map { it.toNews()  }
           }

    suspend fun loadActualNews(ticker: String) {
        if (newsDao.countNewsByTicker(ticker)==0){
            finHubRest.loadCompanyNews(ticker)
                .forEach { netResp->
                   newsDao.insert(netResp.toNewsEntity())
                }
        }
    }
}

private fun CompanyNewsResponse.toNewsEntity() =NewsEntity(
    id = id,
    category = category,
    datetime = Date(datetime*1000),
    headline = headline,
    image = image,
    related = related,
    source = source,
    summary = summary,
    url=url,
)

private fun NewsEntity.toNews()=News(
    id = id,
    category = category,
    datetime = datetime,
    headline = headline,
    image = image,
    related = related,
    source = source,
    summary = summary,
    url=url,
)
