package ru.spb.yakovlev.stocksmonitor.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentFavoritesListBinding

@AndroidEntryPoint
class FavoritesListFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: FavoritesListViewModel by viewModels()
    private val vb: FragmentFavoritesListBinding by viewBinding(FragmentFavoritesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        lifecycleScope.launchWhenResumed {
            viewModel.text.collectLatest {
                vb.tvText.text = it
            }
        }
    }
}