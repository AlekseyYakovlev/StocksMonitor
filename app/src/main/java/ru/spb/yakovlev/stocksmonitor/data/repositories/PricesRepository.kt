package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.*
import ru.spb.yakovlev.stocksmonitor.data.local.daos.PriceDao
import ru.spb.yakovlev.stocksmonitor.data.local.entites.PriceEntity
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.errors.ApiError
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.QuotesResponse
import ru.spb.yakovlev.stocksmonitor.model.DataState
import ru.spb.yakovlev.stocksmonitor.model.Price
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PricesRepository @Inject constructor(
    private val finHubRest: FinHubRestService,
    private val priceDao: PriceDao,
    private val realTimeQuotesProvider: RealTimeQuotesProvider
) {

    suspend fun getPriceFlow(ticker: String): Flow<Price> =
        priceDao.findPriceByTickerFlow(ticker)
            .onEach { priceEntry ->
                if (priceEntry == null) {
                    updatePrice(ticker)
                }
            }
            .filterNotNull()
            .map { it.toPrice() }

    private suspend fun savePriceToDb(price: PriceEntity) {
        priceDao.insert(price)
    }

    suspend fun startPriceMonitoring() {
        realTimeQuotesProvider.initWebSocket()

        realTimeQuotesProvider.prices.collectLatest { state ->
            if (state is DataState.Success) {
                val lastPrice = (state.data).last()
                updatePrice(lastPrice.symbol, lastPrice.price)
            }
        }
    }

    fun stopPriceMonitoring() {
        realTimeQuotesProvider.closeWebSocket()
    }

    private suspend fun updatePrice(ticker: String, newPrice: Double) {
        priceDao.updateLastPrice(ticker, newPrice)
    }

    private suspend fun updatePrice(ticker: String) =
        try {
            Timber.tag("1234567").d("PricesRepository updatePrice $ticker")
            val res = finHubRest.loadDayPrice(ticker)
            savePriceToDb(res.toPriceEntity(ticker))
        } catch (e: ApiError.Forbidden) {
            Timber.tag("PricesRepository 7777777777").e(e)
        }catch (e: Exception) {
            Timber.tag("PricesRepository").e(e)
        }
}

fun Price.toPriceEntity() = PriceEntity(
    ticker = ticker,
    currentPrice = currentPrice,
    previousClosePrice = previousClosePrice,
    lastUpdated = lastUpdated,
)

fun PriceEntity.toPrice() = Price(
    ticker = ticker,
    currentPrice = currentPrice,
    previousClosePrice = previousClosePrice,
    lastUpdated = lastUpdated,
)

fun QuotesResponse.toPriceEntity(ticker: String) = PriceEntity(
    ticker = ticker,
    currentPrice = currentPrice,
    previousClosePrice = previousClosePrice,
    lastUpdated = Date(),
)