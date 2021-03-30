package ru.spb.yakovlev.stocksmonitor.ui.chart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentChartBinding
import ru.spb.yakovlev.stocksmonitor.ui.details.DetailsViewModel

@AndroidEntryPoint
class ChartFragmentFragment : Fragment(R.layout.fragment_chart){

    private val viewModel by viewModels<DetailsViewModel>()
    private val vb by viewBinding(FragmentChartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
vb.tvCurrentPrice.text = "$155.54"
    }
}