package ru.spb.yakovlev.stocksmonitor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import ru.spb.yakovlev.stocksmonitor.model.Price
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetPriceForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateSubsForRealTimePricesUseCase
import ru.spb.yakovlev.stocksmonitor.navigation.Destination
import ru.spb.yakovlev.stocksmonitor.navigation.Navigator
import timber.log.Timber


abstract class MainBaseViewModel(
    private val getPriceForTickerUseCase: GetPriceForTickerUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : ViewModel() {
    abstract suspend fun getStocksList(): Flow<List<StockItemData>>

    suspend fun getPrice(ticker: String): Flow<Price> =
        getPriceForTickerUseCase(ticker)
            .flowOn( Dispatchers.IO)
            .catch { e->
                Timber.tag("Error getPrice").e(e)
            }

    fun getIsFavoriteState(ticker: String): Flow<Boolean> =
        getFavoriteStatusUseCase(ticker)

    fun handleStarClick(ticker: String, isFavorite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase(ticker, isFavorite)
        }

    fun handleOnItemClick(ticker: String, companyName: String, navigator: Navigator) {
        navigator.goTo(Destination.TICKER_DETAILS_FRAGMENT, ticker, companyName)
    }
}
