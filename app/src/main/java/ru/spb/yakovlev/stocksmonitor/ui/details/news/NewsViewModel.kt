package ru.spb.yakovlev.stocksmonitor.ui.details.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetNewsForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateNewsForTickerUseCase
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsForTickerUseCase: GetNewsForTickerUseCase,
    private val updateNewsForTickerUseCase: UpdateNewsForTickerUseCase,
) : ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, _ ->
    }

    fun updateNews(ticker: String) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            updateNewsForTickerUseCase(ticker)
        }
    }

    fun getNews(ticker: String): Flow<List<NewsItemData>> =
        getNewsForTickerUseCase(ticker).map { list ->
            list.map { it.toNewsItemData() }
        }.flowOn(Dispatchers.IO)

}



