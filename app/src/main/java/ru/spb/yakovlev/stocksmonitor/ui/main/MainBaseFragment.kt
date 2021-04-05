package ru.spb.yakovlev.stocksmonitor.ui.main

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentStockItemBinding
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentStocksListBinding
import ru.spb.yakovlev.stocksmonitor.navigation.Navigator
import ru.spb.yakovlev.stocksmonitor.ui.base.BaseRVAdapter
import java.util.*
import javax.inject.Inject


abstract class MainBaseFragment : Fragment(R.layout.fragment_stocks_list) {

    abstract val viewModel: MainBaseViewModel
    private val vb by viewBinding(FragmentStocksListBinding::bind)
    private val stocksListAdapter by lazy(::setupRecyclerViewAdapter)
    private val currencyFormat by lazy(::setupCurrencyFormat)
    private val percentFormat by lazy(::setupPercentFormat)
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

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.getStocksList().collectLatest {
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

                    if (itemData.logo.isNotBlank()) {
                        icLogo.load(itemData.logo) {
                            placeholder(R.drawable.place_holder)
                            error(R.drawable.place_holder)
                        }
                    } else icLogo.load(R.drawable.place_holder)

                    tvTicker.text = itemData.ticker
                    tvCompanyName.text = itemData.compName


                    viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
                        viewModel.getPrice(itemData.ticker).collectLatest { price ->
                            tvCurrentPrice.text = currencyFormat.format(price.currentPrice)

                            val plusSign = if (price.delta > 0) {
                                tvDayDelta.setTextAppearance(R.style.Body_Green)
                                "+"
                            } else {
                                tvDayDelta.setTextAppearance(R.style.Body_Red)
                                ""
                            }
                            tvDayDelta.text = resources.getString(
                                R.string.main__delta_text,
                                plusSign,
                                currencyFormat.format(price.delta),
                                percentFormat.format(price.deltaPercent)
                            )
                        }
                    }
                    viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
                        viewModel.getIsFavoriteState(itemData.ticker).collectLatest {
                            icStar.isChecked = it
                        }
                    }

                    icStar.setOnClickListener {
                        viewModel.handleStarClick(itemData.ticker, !icStar.isChecked)
                    }

                    root.setOnClickListener {
                        viewModel.handleOnItemClick(itemData.ticker, itemData.compName, navigator)
                    }
                }
            },
        )

    private fun setupCurrencyFormat() =
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }


    private fun setupPercentFormat() =
        NumberFormat.getPercentInstance().apply {
            maximumFractionDigits = 2
        }

    private fun setupDarkItemBackground() = ResourcesCompat.getDrawable(
        resources,
        R.drawable.stock_item__shape_dark,
        requireContext().theme
    )
}