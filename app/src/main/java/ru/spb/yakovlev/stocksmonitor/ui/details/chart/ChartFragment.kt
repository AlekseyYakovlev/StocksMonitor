package ru.spb.yakovlev.stocksmonitor.ui.details.chart

import android.content.Context
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentChartBinding
import java.util.*

@AndroidEntryPoint
class ChartFragmentFragment(val ticker: String) : Fragment(R.layout.fragment_chart) {

    private val viewModel by viewModels<ChartViewModel>()
    private val vb by viewBinding(FragmentChartBinding::bind)
    private val currencyFormat by lazy(::setupCurrencyFormat)
    private val percentFormat by lazy(::setupPercentFormat)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupChart()
        setupCurrentPriceBadge()
        setupPeriodSelectorActions()
    }

    private fun setupPeriodSelectorActions() {
        vb.cgPeriod.setOnCheckedChangeListener { _, checkedId ->
            val filter: ChartViewModel.Filter = when (checkedId) {
                R.id.ch_d -> ChartViewModel.Filter.DAY
                R.id.ch_w -> ChartViewModel.Filter.WEEK
                R.id.ch_m -> ChartViewModel.Filter.MONTH
                R.id.ch_6m -> ChartViewModel.Filter.SIX_MONTHS
                R.id.ch_1y -> ChartViewModel.Filter.YEAR
                R.id.ch_all -> ChartViewModel.Filter.ALL
                else -> ChartViewModel.Filter.ALL
            }
            viewModel.handleResolutionSelection(ticker, filter)
        }
    }

    private fun setupCurrentPriceBadge() {
        vb.tvCurrentPrice.text = getString(R.string.chart__loading)

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
            viewModel.getPrice(ticker).collectLatest { price ->
                vb.tvCurrentPrice.text = currencyFormat.format(price.currentPrice)

                val plusSign = if (price.delta > 0) {
                    vb.tvDayDelta.setTextAppearance(R.style.Body_Green)
                    "+"
                } else {
                    vb.tvDayDelta.setTextAppearance(R.style.Body_Red)
                    ""
                }
                vb.tvDayDelta.text = resources.getString(
                    R.string.main__delta_text,
                    plusSign,
                    currencyFormat.format(price.delta),
                    percentFormat.format(price.deltaPercent)
                )
            }
        }
    }

    private fun setupChart() {

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
            viewModel.chartData.collectLatest {
                when (it) {
                    ChartViewModel.ChartState.Initial -> {
                        vb.tvChartStatus.text = getString(R.string.chart__loading)
                    }
                    ChartViewModel.ChartState.NoData -> {
                        vb.pricesChart.isInvisible = true
                        vb.tvChartStatus.text = getString(R.string.chart__no_data)
                    }
                    is ChartViewModel.ChartState.Success -> {
                        styleChart(vb.pricesChart)
                        vb.tvChartStatus.text = ""
                        vb.pricesChart.isVisible = true
                        vb.pricesChart.data = LineData(styleLineDataSet(requireContext(), it.data))
                        vb.pricesChart.invalidate()
                    }
                }
            }
        }
        setChartDefaultValue()
    }

    private fun setChartDefaultValue() {
        viewModel.handleResolutionSelection(ticker, ChartViewModel.Filter.DAY)
    }
}

private fun styleLineDataSet(context: Context, lineDataSet: LineDataSet) = lineDataSet.apply {
    setDrawValues(false)
    lineWidth = 2f
    isHighlightEnabled = true
    setDrawHighlightIndicators(false)
    setDrawCircles(false)
    mode = LineDataSet.Mode.HORIZONTAL_BEZIER

    color = ContextCompat.getColor(context, R.color.black)
    valueTextColor = ContextCompat.getColor(context, R.color.red)

    setDrawFilled(true)
    fillDrawable = ContextCompat.getDrawable(context, R.drawable.bg_spark_line)
}

private fun styleChart(lineChart: LineChart) = lineChart.apply {
    axisRight.isEnabled = false
    axisLeft.apply {
        isEnabled = false
    }
    xAxis.apply {
        isEnabled = false
    }
    setTouchEnabled(true)
    isDragXEnabled = true
    isDragYEnabled = false
    setScaleEnabled(false)
    setPinchZoom(false)
    setViewPortOffsets(0f, 0f, 0f, 0f)
    description = null
    legend.isEnabled = false
    animateX(1000)
}

private fun setupCurrencyFormat() =
    NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 0
    }

private fun setupPercentFormat() =
    NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = 2
    }