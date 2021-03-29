package ru.spb.yakovlev.stocksmonitor.ui.details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentDetailsBinding
import ru.spb.yakovlev.stocksmonitor.navigation.Navigator
import ru.spb.yakovlev.stocksmonitor.ui.main.VPCollectionAdapterOld
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel>()
    private val vb by viewBinding(FragmentDetailsBinding::bind)
    private val ticker by lazy { args.ticker }

    private lateinit var collectionAdapter: VPCollectionAdapterOld

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
       // val ticker = args.ticker

        vb.icBack.setOnClickListener {
            navigator.back()
        }

        vb.tvTicker.text = args.ticker

        lifecycleScope.launchWhenResumed {
            viewModel.getIsFavoriteState(ticker).collectLatest {
                vb.icStar.isChecked = it
            }
        }
        vb.icStar.setOnClickListener {
            viewModel.handleStarClick(ticker,!vb.icStar.isChecked)
        }
    }

    private fun setupViewPager() {
        collectionAdapter = VPCollectionAdapterOld(this)
        vb.pager.adapter = collectionAdapter

        TabLayoutMediator(vb.tabLayout, vb.pager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.main__title_stocks)
                1 -> resources.getString(R.string.main__title_favorites)
                else -> "Error"
            }
        }.attach()

        vb.tabLayout.apply {
            (0 until tabCount).forEach { i ->
                getTabAt(i)?.also { tab ->
                    val tabTextView = TextView(requireContext())
                    tab.customView = tabTextView
                    with(tabTextView) {
                        if (tab.isSelected) setTextAppearance(R.style.Tabs_H1)
                        else {
                            setTextAppearance(R.style.Tabs_H2)
                        }
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        text = tab.text
                    }
                }
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    (tab.customView as TextView).setTextAppearance(R.style.Tabs_H1)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    (tab.customView as TextView).setTextAppearance(R.style.Tabs_H2)
                }

                override fun onTabReselected(tab: TabLayout.Tab) = Unit
            })
        }
    }
}