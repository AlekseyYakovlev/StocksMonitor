package ru.spb.yakovlev.stocksmonitor.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentSearchSuggestionsBinding

@AndroidEntryPoint
class SearchSuggestionsFragment : Fragment(R.layout.fragment_search_suggestions) {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val vb by viewBinding(FragmentSearchSuggestionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        addChips()
    }

    private fun addChips() {
        viewModel.getPopular1().forEach {
            vb.cgPopular1.addView(
                addNewChip(it)
            )
        }
        viewModel.getPopular2().forEach {
            vb.cgPopular2.addView(
                addNewChip(it)
            )
        }

        lifecycleScope.launchWhenResumed {
            viewModel.getRecent().collectLatest {
                vb.cgRecent1.removeAllViews()
                vb.cgRecent2.removeAllViews()
                it.forEachIndexed { index, text ->
                    if (index % 2 == 0) vb.cgRecent1.addView(addNewChip(text))
                    else vb.cgRecent2.addView(addNewChip(text))
                }
            }
        }
    }

    private fun addNewChip(chipText: String) = Chip(requireContext()).apply {
        text = chipText
        setOnClickListener {
            viewModel.handleChipClick(chipText)
        }
    }

}

