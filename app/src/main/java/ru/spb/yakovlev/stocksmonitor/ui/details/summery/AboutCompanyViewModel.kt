package ru.spb.yakovlev.stocksmonitor.ui.details.summery

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.model.Company
import ru.spb.yakovlev.stocksmonitor.model.interactors.GetCompanyByTickerUseCase
import javax.inject.Inject

@HiltViewModel
class AboutCompanyViewModel @Inject constructor(
    private val getCompanyByTickerUseCase: GetCompanyByTickerUseCase
) : ViewModel() {

    fun getCompanyInfo(ticker:String): Flow<Company> =
        getCompanyByTickerUseCase(ticker)

}




