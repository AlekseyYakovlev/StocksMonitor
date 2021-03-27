package ru.spb.yakovlev.stocksmonitor.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {
    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState: StateFlow<SearchScreenState> = _searchScreenState


    fun activateSearch() {
        val currentState = _searchScreenState.value
        if (!currentState.isActive) {
            _searchScreenState.value = _searchScreenState.value.copy(isActive = true)
        }
    }

    fun deactivateSearch() {
        _searchScreenState.value = _searchScreenState.value.copy(isActive = false)
    }

    fun handleTextInput(query: String) {
        _searchScreenState.value = _searchScreenState.value.copy(query = query)
    }

    fun handleClean() {
        _searchScreenState.value = _searchScreenState.value.copy(query = "")
    }

    fun handleBackClick() {
        _searchScreenState.value = _searchScreenState.value.copy(isActive = false, query = "")
    }


    private val _text = MutableStateFlow("This is FavoritesList Fragment")
    val text: StateFlow<String> = _text
}

data class SearchScreenState(
    val isActive: Boolean = false,
    val query: String = "",
    val areSuggestionsShown: Boolean = false,
)