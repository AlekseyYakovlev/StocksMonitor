package ru.spb.yakovlev.stocksmonitor.ui.stocks

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import ru.spb.yakovlev.stocksmonitor.data.repositories.MokkData
import ru.spb.yakovlev.stocksmonitor.ui.main.StockItemData
import ru.spb.yakovlev.stocksmonitor.ui.main.toStockItemData
import javax.inject.Inject

@HiltViewModel
class StocksListViewModel @Inject constructor(
    private val mokkData: MokkData,

    ) : ViewModel() {
    val stocksList: Flow<List<StockItemData>> =
        mokkData.stocks.combine(mokkData.favorites){stocksList, favorites ->
            stocksList.map { it.toStockItemData(it in favorites) }
        }

    fun handleFavor(ticker: String, isFavorite: Boolean): Boolean =
        mokkData.updateFavorites(ticker, isFavorite)

    fun getIsFavoriteState(ticker: String): Flow<Boolean> =
        mokkData.getIsFavoriteState(ticker)

}