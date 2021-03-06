package ru.spb.yakovlev.stocksmonitor.ui.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentMainBinding
import ru.spb.yakovlev.stocksmonitor.ui.main.favorites.FavoritesListFragment
import ru.spb.yakovlev.stocksmonitor.ui.main.search.SearchResultsFragment
import ru.spb.yakovlev.stocksmonitor.ui.main.search.SearchScreenState
import ru.spb.yakovlev.stocksmonitor.ui.main.search.SearchViewModel
import ru.spb.yakovlev.stocksmonitor.ui.main.search.VisibleScreen
import ru.spb.yakovlev.stocksmonitor.ui.main.stocks.StocksListFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val vb by viewBinding(FragmentMainBinding::bind)

    private lateinit var collectionAdapter: VPCollectionAdapterOld

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupViewPager()
        setupScreensVisibility()
    }

    private fun setupScreensVisibility() {
        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
            viewModel.searchScreenState.collectLatest(::renderScreenState)
        }
    }

    private fun renderScreenState(state: SearchScreenState) {
        when(state.visibleScreen){
            VisibleScreen.DEFAULT -> {
                vb.tabLayout.isVisible = true
                vb.pagerContainer.isVisible = true
                vb.searchSuggestionsContainer.isVisible = false
                vb.searchResultsContainer.isVisible = false
            }
            VisibleScreen.SEARCH_RESULTS -> {
                vb.tabLayout.isVisible = false
                vb.pagerContainer.isVisible = false
                vb.searchSuggestionsContainer.isVisible = false
                vb.searchResultsContainer.isVisible = true
            }
            VisibleScreen.SEARCH_SUGGESTIONS -> {
                vb.tabLayout.isVisible = false
                vb.pagerContainer.isVisible = false
                vb.searchSuggestionsContainer.isVisible = true
                vb.searchResultsContainer.isVisible = false
            }
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

class VPCollectionAdapterOld(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> StocksListFragment()
            1 -> FavoritesListFragment()
            else -> SearchResultsFragment()
        }

}
