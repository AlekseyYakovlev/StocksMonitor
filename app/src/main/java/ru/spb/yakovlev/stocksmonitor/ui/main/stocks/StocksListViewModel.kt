package ru.spb.yakovlev.stocksmonitor.ui.main.stocks

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetPriceForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateSubsForRealTimePricesUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.main.GetDefaultCompanyListUseCase
import ru.spb.yakovlev.stocksmonitor.ui.main.MainBaseViewModel
import ru.spb.yakovlev.stocksmonitor.ui.main.StockItemData
import ru.spb.yakovlev.stocksmonitor.ui.main.toStockItemData
import javax.inject.Inject

@HiltViewModel
class StocksListViewModel @Inject constructor(
    private val getDefaultCompanyListUseCase: GetDefaultCompanyListUseCase,
    private val updateSubsForRealTimePricesUseCase: UpdateSubsForRealTimePricesUseCase,
    getPriceForTickerUseCase: GetPriceForTickerUseCase,
    getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : MainBaseViewModel(
    getPriceForTickerUseCase,
    getFavoriteStatusUseCase,
    updateFavoriteStatusUseCase,
) {
    override suspend fun getStocksList(): Flow<List<StockItemData>> =
        getDefaultCompanyListUseCase()
            .map { list ->
                updateSubsForRealTimePricesUseCase(list.map { it.ticker })
                list.map { it.toStockItemData() }
            }
            .flowOn(Dispatchers.IO)
}