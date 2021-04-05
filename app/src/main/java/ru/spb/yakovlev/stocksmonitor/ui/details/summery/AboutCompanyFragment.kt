package ru.spb.yakovlev.stocksmonitor.ui.details.summery

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentAboutCompanyBinding
import java.util.*

@AndroidEntryPoint
class AboutCompanyFragment(val ticker: String) : Fragment(R.layout.fragment_about_company) {

    private val viewModel by viewModels<AboutCompanyViewModel>()
    private val vb by viewBinding(FragmentAboutCompanyBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
            viewModel.getCompanyInfo(ticker).collectLatest { company ->
                with(company) {
                    vb.tvName.text = name
                    vb.tvCountry.text = country
                    vb.tvCurrency.text = currency
                    vb.tvExchange.text = exchange
                    vb.tvIpo.text = ipo
                    vb.tvMarketCapitalization.text =
                        setupCurrencyFormat().format(marketCapitalization)
//                    vb.tvShareOutstanding.text = setupPercentFormat().format(shareOutstanding)
                    vb.tvPhone.text = phone
                    vb.tvWebUrl.text = webUrl
                    vb.tvIndustry.text = industry
                }
            }
        }

    }


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