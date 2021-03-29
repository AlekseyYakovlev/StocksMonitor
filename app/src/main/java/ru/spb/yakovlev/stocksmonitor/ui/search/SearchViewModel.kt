package ru.spb.yakovlev.stocksmonitor.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.spb.yakovlev.stocksmonitor.data.repositories.MokkData
import ru.spb.yakovlev.stocksmonitor.ui.main.StockItemData
import ru.spb.yakovlev.stocksmonitor.ui.main.toStockItemData
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mokkData: MokkData
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState: StateFlow<SearchScreenState> = _searchScreenState

    // search results
    val stocksList: Flow<List<StockItemData>> = searchScreenState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500)
        .mapLatest { query ->
            mokkData.getSearchResults(query).map { it.toStockItemData() }
        }.flowOn(Dispatchers.IO)


    fun getPopular1(): List<String> =
        mokkData.popularQueries.filterIndexed { index, _ -> index % 2 == 0 }

    fun getPopular2(): List<String> =
        mokkData.popularQueries.filterIndexed { index, _ -> index % 2 != 0 }



    fun getRecent(): Flow<List<String>> =
        mokkData.resentQueries

    fun handleChipClick(chipText: String) {
        _searchScreenState.value = _searchScreenState.value.copy(
            isActive = true,
            query = chipText,
            areSuggestionsShown = false,
            areResultsShown = true,
            isDefaultScreenShown = false,
        )
    }

    fun handleQuerySubmit(query: String?) {
        handleQueryChange(query)
        query?.let { saveQueryHistory(it) }
    }

    fun handleQueryChange(query: String?) {
        if (query.isNullOrBlank()) {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = true,
                    query = "",
                    areSuggestionsShown = true,
                    areResultsShown = false,
                    isDefaultScreenShown = false,
                )
        } else {
            _searchScreenState.value =
                _searchScreenState.value.copy(
                    isActive = true,
                    query = query,
                    areSuggestionsShown = false,
                    areResultsShown = true,
                    isDefaultScreenShown = false,
                )
        }
    }

    fun handleFocusReceived() {
        _searchScreenState.value =
            _searchScreenState.value.copy(
                isActive = true,
                areSuggestionsShown = true,
                areResultsShown = false,
                isDefaultScreenShown = false,
            )
    }

    fun handleFocusLost() {
        // _searchScreenState.value = _searchScreenState.value.copy(isActive = false)
    }

    fun handleBackClick() {
        _searchScreenState.value =
            _searchScreenState.value.copy(
                isActive = false,
                query = "",
                areSuggestionsShown = false,
                areResultsShown = false,
                isDefaultScreenShown = true,
            )
    }

    private fun saveQueryHistory(query: String) {
        if (query.isNotBlank()) {
            mokkData.updateResentQueries(query)
        }
    }

    fun handleStarClick(ticker: String, isFavorite: Boolean): Boolean =
        mokkData.updateFavorites(ticker, isFavorite)

    fun getIsFavoriteState(ticker: String): Flow<Boolean> =
        mokkData.getIsFavoriteState(ticker)

}

data class SearchScreenState(
    val isActive: Boolean = false,
    val query: String = "",
    val areSuggestionsShown: Boolean = false,
    val areResultsShown: Boolean = false,
    val isDefaultScreenShown: Boolean = true,
)