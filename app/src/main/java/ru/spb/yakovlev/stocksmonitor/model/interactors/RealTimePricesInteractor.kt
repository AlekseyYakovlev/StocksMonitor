package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.RealTimeQuotesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTimePricesInteractor @Inject constructor(
private val realTimeQuotesProvider: RealTimeQuotesProvider
) {
    fun start()=realTimeQuotesProvider.initWebSocket()

    fun stop()=realTimeQuotesProvider.closeWebSocket()
}