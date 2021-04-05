package ru.spb.yakovlev.stocksmonitor.ui.details.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.spb.yakovlev.stocksmonitor.data.remote.errors.NoNetworkError
import ru.spb.yakovlev.stocksmonitor.model.Price
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetHistoryDataForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetPriceForTickerUseCase
import ru.spb.yakovlev.stocksmonitor.model.interactors.UpdateSubsForRealTimePricesUseCase
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val getPriceForTickerUseCase: GetPriceForTickerUseCase,
    private val getHistoryDataForTickerUseCase: GetHistoryDataForTickerUseCase,
    private val updateSubsForRealTimePricesUseCase: UpdateSubsForRealTimePricesUseCase,
) : ViewModel() {

    private val errorHandler = CoroutineExceptionHandler{ _, throwable ->
        when(throwable){
            is NoNetworkError -> _chartData.value = ChartState.NoInternet
            else -> {}
        }
    }

    suspend fun getPrice(ticker: String): Flow<Price> =
        getPriceForTickerUseCase(ticker)
            .flowOn(Dispatchers.IO)
            .also { updateSubsForRealTimePricesUseCase(listOf(ticker)) }

    private val _chartData = MutableStateFlow<ChartState>(ChartState.Initial)
    val chartData: StateFlow<ChartState> = _chartData


    fun handleResolutionSelection(ticker: String, filterSettings: Filter) {
        _chartData.value = ChartState.Initial
        val (filterDate, resolution) = when (filterSettings) {
            Filter.DAY -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "15"
            }
            Filter.WEEK -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "60"
            }
            Filter.MONTH -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "D"
            }
            Filter.SIX_MONTHS -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "W"
            }
            Filter.YEAR -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "W"
            }
            Filter.ALL -> {
                GregorianCalendar().apply {
                    add(GregorianCalendar.DAY_OF_WEEK, -1)
                }.time.time / 1000 to "M"
            }
        }

        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val historyData = getHistoryDataForTickerUseCase(
                ticker,
                resolution,
                filterDate,
                Date().time / 1000
            )

            val entriesList = historyData.dates
                .zip(historyData.prices)
                .map {
                    Entry(it.first.toFloat(), it.second.toFloat())
                }

            if (entriesList.size <= 1) _chartData.value = ChartState.NoData
            else _chartData.value = ChartState.Success(LineDataSet(entriesList, ""))
        }



    }

    enum class Filter {
        DAY,
        WEEK,
        MONTH,
        SIX_MONTHS,
        YEAR,
        ALL
    }

    sealed class ChartState {
        object Initial : ChartState()
        data class Success(val data: LineDataSet) : ChartState()
        object NoData : ChartState()
        object NoInternet : ChartState()
    }

}

private fun <T> List<T>.double(): List<T> =
    this + this


