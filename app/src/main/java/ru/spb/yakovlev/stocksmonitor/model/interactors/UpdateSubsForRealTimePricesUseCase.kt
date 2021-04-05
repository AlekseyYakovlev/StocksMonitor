package ru.spb.yakovlev.stocksmonitor.model.interactors

import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.repositories.RealTimeQuotesProvider
import ru.spb.yakovlev.stocksmonitor.model.Price
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateSubsForRealTimePricesUseCase @Inject constructor(
private val realTimeQuotesProvider: RealTimeQuotesProvider
) {
    operator fun invoke(tickers:List<String>) =
        realTimeQuotesProvider.updateSubscriptionsList(tickers)
}