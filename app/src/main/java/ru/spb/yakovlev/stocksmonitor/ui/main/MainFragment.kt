package ru.spb.yakovlev.stocksmonitor.ui.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentMainBinding
import ru.spb.yakovlev.stocksmonitor.ui.favorites.FavoritesListFragment
import ru.spb.yakovlev.stocksmonitor.ui.search.SearchResultsFragment
import ru.spb.yakovlev.stocksmonitor.ui.stocks.StocksListFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private val vb by viewBinding(FragmentMainBinding::bind)

    private lateinit var collectionAdapter: VPCollectionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupViewPager()

    }

    private fun setupSearch(){



    }

    private fun setupViewPager() {
        collectionAdapter = VPCollectionAdapter(this)
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
                getTabAt(i)?.also {tab->
                    val tabTextView = TextView(requireContext())
                    tab.customView = tabTextView
                   with(tabTextView)  {
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

class VPCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> StocksListFragment()
            1 -> FavoritesListFragment()
            else -> SearchResultsFragment()
        }

}
