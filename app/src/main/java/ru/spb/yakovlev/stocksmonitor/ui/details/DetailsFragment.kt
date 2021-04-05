package ru.spb.yakovlev.stocksmonitor.ui.details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentDetailsBinding
import ru.spb.yakovlev.stocksmonitor.navigation.Navigator
import ru.spb.yakovlev.stocksmonitor.ui.details.chart.ChartFragmentFragment
import ru.spb.yakovlev.stocksmonitor.ui.details.news.NewsFragment
import ru.spb.yakovlev.stocksmonitor.ui.details.summery.AboutCompanyFragment
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel>()
    private val vb by viewBinding(FragmentDetailsBinding::bind)
    private val ticker by lazy { args.ticker }

    private lateinit var collectionAdapter: VPCollectionAdapterDetails

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        vb.icBack.setOnClickListener {
            navigator.back()
        }

        vb.tvTicker.text = args.ticker
        vb.tvCompanyName.text = args.companyName

        lifecycleScope.launchWhenResumed {
            viewModel.getIsFavoriteState(ticker).collectLatest {
                vb.icStar.isChecked = it
            }
        }
        vb.icStar.setOnClickListener {
            viewModel.handleStarClick(ticker, !vb.icStar.isChecked)
        }
        setupViewPager()
    }

    private fun setupViewPager() {
        collectionAdapter = VPCollectionAdapterDetails(this, args.ticker)
        vb.pager.adapter = collectionAdapter

        TabLayoutMediator(vb.tabLayout, vb.pager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.details__title_chart)
                1 -> resources.getString(R.string.details__title_summary)
                2 -> resources.getString(R.string.details__title_news)
                else -> "Error"
            }
        }.attach()

        vb.tabLayout.apply {
            (0 until tabCount).forEach { i ->
                getTabAt(i)?.also { tab ->
                    val tabTextView = TextView(requireContext())
                    tab.customView = tabTextView
                    with(tabTextView) {
                        if (tab.isSelected) setTextAppearance(R.style.Tabs_H2_Black)
                        else {
                            setTextAppearance(R.style.Tabs_H3)
                        }
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        text = tab.text
                    }
                }
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    (tab.customView as TextView).setTextAppearance(R.style.Tabs_H2_Black)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    (tab.customView as TextView).setTextAppearance(R.style.Tabs_H3)
                }

                override fun onTabReselected(tab: TabLayout.Tab) = Unit
            })
        }
    }
}

class VPCollectionAdapterDetails(fragment: Fragment, val ticker: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> ChartFragmentFragment(ticker)
            1 -> AboutCompanyFragment(ticker)
            2 -> NewsFragment(ticker)
            else -> AboutCompanyFragment(ticker)
        }

}