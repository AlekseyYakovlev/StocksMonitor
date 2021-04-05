package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.PricesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTimePricesInteractor @Inject constructor(
    private val pricesRepository: PricesRepository
) {
    suspend fun start() = pricesRepository.startPriceMonitoring()

    fun stop() = pricesRepository.stopPriceMonitoring()
}