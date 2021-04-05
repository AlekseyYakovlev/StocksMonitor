package ru.spb.yakovlev.stocksmonitor.ui.main.favorites

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.ui.main.MainBaseFragment

@AndroidEntryPoint
class FavoritesListFragment : MainBaseFragment() {
    override val viewModel by viewModels<FavoritesListViewModel>()
}