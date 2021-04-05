package ru.spb.yakovlev.stocksmonitor.ui.main.stocks

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.ui.main.MainBaseFragment

@AndroidEntryPoint
class StocksListFragment : MainBaseFragment() {
    override val viewModel by viewModels<StocksListViewModel>()
}