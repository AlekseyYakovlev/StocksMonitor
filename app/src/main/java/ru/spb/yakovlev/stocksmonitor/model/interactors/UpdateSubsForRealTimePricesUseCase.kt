package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.RealTimeQuotesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateSubsForRealTimePricesUseCase @Inject constructor(
    private val realTimeQuotesProvider: RealTimeQuotesProvider
) {
    operator fun invoke(tickers: List<String>) =
        realTimeQuotesProvider.updateSubscriptionsList(tickers)
}