package ru.spb.yakovlev.stocksmonitor.ui.search

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentStockItemBinding
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentStocksListBinding
import ru.spb.yakovlev.stocksmonitor.navigation.Destination
import ru.spb.yakovlev.stocksmonitor.navigation.Navigator
import ru.spb.yakovlev.stocksmonitor.ui.base.BaseRVAdapter
import ru.spb.yakovlev.stocksmonitor.ui.main.StockItemData
import ru.spb.yakovlev.stocksmonitor.ui.stocks.StocksListViewModel
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_stocks_list) {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val vb: FragmentStocksListBinding by viewBinding(FragmentStocksListBinding::bind)
    private val stocksListAdapter by lazy(::setupRecyclerViewAdapter)
    private val currencyFormat by lazy(::setupCurrencyFormat)
    private val darkItemBackground by lazy(::setupDarkItemBackground)

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        vb.rvStocksList.apply {
            adapter = stocksListAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stocksList.collectLatest {
                stocksListAdapter.updateData(it)
            }
        }
    }

    private fun setupRecyclerViewAdapter() =
        BaseRVAdapter<FragmentStockItemBinding, StockItemData>(
            viewHolderInflater = { layoutInflater, parent, attachToParent ->
                FragmentStockItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            viewHolderBinder = { holder, itemData, position ->
                with(holder) {

                    if (position % 2 == 0) root.background = darkItemBackground

                    icLogo.load(itemData.logo)
                    tvTicker.text = itemData.ticker
                    tvCompanyName.text = itemData.compName
                    tvCurrentPrice.text = currencyFormat.format(itemData.price)
                    tvDayDelta.text = currencyFormat.format(itemData.delta)

                    lifecycleScope.launchWhenResumed {
                        viewModel.getIsFavoriteState(itemData.ticker).collectLatest {
                            icStar.isChecked = it
                        }
                    }

                    icStar.setOnClickListener {
                        viewModel.handleStarClick(itemData.ticker, !icStar.isChecked)
                    }

                    root.setOnClickListener {
                        navigator.goTo(Destination.TICKER_DETAILS_FRAGMENT, itemData.ticker)
                    }
                }
            },
        )

    private fun setupCurrencyFormat() =
        NumberFormat.getCurrencyInstance(Locale.US)

    private fun setupDarkItemBackground() = ResourcesCompat.getDrawable(
        resources,
        R.drawable.stock_item__shape_dark,
        requireContext().theme
    )
}