package ru.spb.yakovlev.stocksmonitor.ui.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(

) : ViewModel() {
    private val _text = MutableStateFlow("This is Details Fragment")
    val text: StateFlow<String> = _text

    fun updateData(ticker: String) {
        _text.value = ticker
    }
}