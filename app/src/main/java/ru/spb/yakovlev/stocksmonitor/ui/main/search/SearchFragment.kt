package ru.spb.yakovlev.stocksmonitor.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentSearchBinding
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val vb by viewBinding(FragmentSearchBinding::bind)
    private var lastScreenState = SearchScreenState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupListeners()
        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED) {
            viewModel.searchScreenState.collectLatest(::renderSearchState)
        }
    }

    private fun renderSearchState(state: SearchScreenState) {
        if (state.isActive) {
            vb.svSearchQuery.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.search_shape_selected, null)
            vb.icBack.isVisible = true
        } else {
            vb.svSearchQuery.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.search_shape, null)
            vb.icBack.isVisible = false
            vb.svSearchQuery.clearFocus()
        }

        if (lastScreenState.query != state.query) {
            if (vb.svSearchQuery.query.toString() != state.query) {
                vb.svSearchQuery.setQuery(state.query, true)
            }
        }

        lastScreenState = state.copy()
    }


    private fun setupListeners() {
        vb.svSearchQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleQuerySubmit(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.handleQueryChange(query)
                return true
            }
        })

        vb.icBack.setOnClickListener {
            viewModel.handleBackIconClick()
            vb.svSearchQuery.clearFocus()
        }

        vb.svSearchQuery.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.handleFocusReceived()
            } else {
                viewModel.handleFocusLost()
            }
        }
    }

}
