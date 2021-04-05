package ru.spb.yakovlev.stocksmonitor.model.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.repositories.CompaniesRepository
import ru.spb.yakovlev.stocksmonitor.data.repositories.IndicesRepository
import ru.spb.yakovlev.stocksmonitor.data.repositories.PricesRepository
import ru.spb.yakovlev.stocksmonitor.model.Company
import ru.spb.yakovlev.stocksmonitor.model.Price
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPriceForTickerUseCase @Inject constructor(
    private val pricesRepository: PricesRepository
) {
    suspend operator fun invoke(ticker:String): Flow<Price> =
        pricesRepository.getPriceFlow(ticker)
}