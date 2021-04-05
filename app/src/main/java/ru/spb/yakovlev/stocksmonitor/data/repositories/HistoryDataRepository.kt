package ru.spb.yakovlev.stocksmonitor.data.repositories

import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.StockCandlesResponse
import ru.spb.yakovlev.stocksmonitor.model.HistoryData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryDataRepository @Inject constructor(
    private val finHubRest: FinHubRestService
) {

    suspend fun loadHistoryData(
        ticker: String,
        resolution: String,
        from: Long,
        to: Long
    ): HistoryData =
        finHubRest.loadCandles(ticker, resolution, from, to)
            .toHistoryData(ticker)


}

private fun StockCandlesResponse.toHistoryData(ticker: String) = HistoryData(
    ticker = ticker,
    dates = timestampList,
    prices = closePricesList,
    status = if (status == "ok") HistoryData.Status.OK else HistoryData.Status.ERROR
)