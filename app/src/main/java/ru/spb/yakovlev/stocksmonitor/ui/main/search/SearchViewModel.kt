package ru.spb.yakovlev.stocksmonitor.ui.main.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetPriceForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateFavoriteStatusUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.main.GetSearchResultsUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.main.search.GetPopularQueriesUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.main.search.GetResentQueriesUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.main.search.UpdateResentQueriesUseCase
import ru.spb.yakovlev.stocksmonitor.ui.main.MainBaseViewModel
import ru.spb.yakovlev.stocksmonitor.ui.main.StockItemData
import ru.spb.yakovlev.stocksmonitor.ui.main.toStockItemData
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getPopularQueriesUseCase: GetPopularQueriesUseCase,
    private val getResentQueriesUseCase: GetResentQueriesUseCase,
    private val updateResentQueriesUseCase: UpdateResentQueriesUseCase,
    getPriceForTickerUseCase: GetPriceForTickerUseCase,
    getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : MainBaseViewModel(
    getPriceForTickerUseCase,
    getFavoriteStatusUseCase,
    updateFavoriteStatusUseCase,
) {
    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState: StateFlow<SearchScreenState> = _searchScreenState

    // search results
    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    override suspend fun getStocksList(): Flow<List<StockItemData>> = searchScreenState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500)
        .mapLatest { query ->
            getSearchResultsUseCase(query,4).map { it.toStockItemData() }
        }.flowOn(Dispatchers.IO)

    fun getPopular(): List<String> =
        getPopularQueriesUseCase()

    fun getRecent(): Flow<List<String>> =
        getResentQueriesUseCase()

    fun handleChipClick(chipText: String) {
        _searchScreenState.value = _searchScreenState.value.copy(
            isActive = true,
            query = chipText,
            visibleScreen = VisibleScreen.SEARCH_RESULTS,
        )
    }

    fun handleQuerySubmit(query: String?) {
        handleQueryChange(query)
        query?.let { saveQueryHistory(it) }
    }

    fun handleQueryChange(query: String?) {
        if (_searchScreenState.value.isActive){
            if (query.isNullOrBlank()) {
                _searchScreenState.value =
                    _searchScreenState.value.copy(
                        isActive = true,
                        query = "",
                        visibleScreen = VisibleScreen.SEARCH_SUGGESTIONS,
                    )
            } else {
                _searchScreenState.value =
                    _searchScreenState.value.copy(
                        isActive = true,
                        query = query,
                        visibleScreen = VisibleScreen.SEARCH_RESULTS,
                    )
            }
        }
    }

    fun handleFocusReceived(){
        if (_searchScreenState.value.query.isBlank()) {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = true,
                    visibleScreen = VisibleScreen.SEARCH_SUGGESTIONS,
                )
        } else {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = true,
                    visibleScreen = VisibleScreen.SEARCH_RESULTS,
                )
        }
    }

    fun handleFocusLost() {
        if (_searchScreenState.value.query.isBlank()) {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = false,
                    visibleScreen = VisibleScreen.DEFAULT,
                )
        } else {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = true,
                    visibleScreen = VisibleScreen.SEARCH_RESULTS,
                )
        }
    }

    fun handleBackIconClick() {
        clearSearch()
    }

    private fun clearSearch(){
        _searchScreenState.value =
            _searchScreenState.value.copy(
                isActive = false,
                query = "",
                visibleScreen = VisibleScreen.DEFAULT,
            )
    }

    private fun saveQueryHistory(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch (Dispatchers.IO) {
                updateResentQueriesUseCase(query)
            }
        }
    }

}

data class SearchScreenState(
    val isActive: Boolean = false,
    val query: String = "",
    val visibleScreen: VisibleScreen = VisibleScreen.DEFAULT,
)
enum class VisibleScreen{
    DEFAULT,
    SEARCH_RESULTS,
    SEARCH_SUGGESTIONS
}