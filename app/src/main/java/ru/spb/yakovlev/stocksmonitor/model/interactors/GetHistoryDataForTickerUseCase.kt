package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.HistoryDataRepository
import ru.spb.yakovlev.stocksmonitor.model.HistoryData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHistoryDataForTickerUseCase @Inject constructor(
    private val historyDataRepository: HistoryDataRepository
) {
    suspend operator fun invoke(
        ticker: String,
        resolution: String,
        from: Long,
        to: Long
    ): HistoryData =
        historyDataRepository.loadHistoryData(ticker, resolution, from, to)
}