package ru.spb.yakovlev.stocksmonitor.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {
    private val _text = MutableStateFlow("This is FavoritesList Fragment")
    val text: StateFlow<String> = _text
}