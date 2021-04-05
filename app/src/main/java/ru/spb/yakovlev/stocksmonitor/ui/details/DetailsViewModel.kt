package ru.spb.yakovlev.stocksmonitor.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.spb.yakovlev.stocksmonitor.model.Company
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetCompanyByTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateFavoriteStatusUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : ViewModel() {

    fun getIsFavoriteState(ticker: String): Flow<Boolean> =
        getFavoriteStatusUseCase(ticker)

    fun handleStarClick(ticker: String, isFavorite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase(ticker, isFavorite)
        }

}