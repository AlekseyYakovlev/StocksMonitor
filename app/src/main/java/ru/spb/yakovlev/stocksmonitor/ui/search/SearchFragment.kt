package ru.spb.yakovlev.stocksmonitor.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentSearchBinding

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val vb by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        setupListeners()

        lifecycleScope.launchWhenResumed {
            viewModel.searchScreenState.collectLatest(::renderScreen)
        }

    }

    private fun setupListeners() {


        vb.etSearchQuery.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

             return   false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                Toast.makeText(requireContext(), newText, Toast.LENGTH_SHORT).show()
              return  true
            }

        })

        vb.icBack.setOnClickListener {
           // viewModel.handleBackClick()
            vb.etSearchQuery.clearFocus()
        }

        vb.etSearchQuery.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                vb.searchRoot.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.search_shape_selected, null)
                vb.icBack.isVisible = true
            } else {

                vb.searchRoot.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.search_shape, null)
                vb.icBack.isGone = true
            }
          //  Toast.makeText(requireContext(), hasFocus.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(requireContext(),InputMethodManager::class.java)
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    fun hideSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(requireContext(),InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun renderScreen(screenState: SearchScreenState) {
//        with(screenState) {
//            if (isActive) {
//                vb.icBack.isVisible = true
//                vb.icSearch.isInvisible = true
//                vb.searchRoot.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.search_shape_selected, null)
//
//            } else {
//                vb.icBack.isGone = true
//                vb.icSearch.isVisible = true
//                vb.searchRoot.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.search_shape, null)
//            }

        //  if (query.isBlank()) vb.etSearchQuery.setText(query)
//        }
    }
}

