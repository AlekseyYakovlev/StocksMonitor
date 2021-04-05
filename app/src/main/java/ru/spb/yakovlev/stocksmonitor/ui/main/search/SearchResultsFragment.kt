package ru.spb.yakovlev.stocksmonitor.ui.main.search

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.ui.main.MainBaseFragment

@AndroidEntryPoint
class SearchResultsFragment : MainBaseFragment() {
    override val viewModel by activityViewModels<SearchViewModel>()
}