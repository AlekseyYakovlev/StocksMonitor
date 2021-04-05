package ru.spb.yakovlev.stocksmonitor.ui.details.news

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentNewsItemBinding
import ru.spb.yakovlev.stocksmonitor.databinding.FragmentNewsListBinding
import ru.spb.yakovlev.stocksmonitor.ui.base.BaseRVAdapter
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewsFragment(val ticker: String) : Fragment(R.layout.fragment_news_list) {

    private val viewModel by viewModels<NewsViewModel>()
    private val vb by viewBinding(FragmentNewsListBinding::bind)
    private val newsListAdapter by lazy(::setupRecyclerViewAdapter)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel.updateNews(ticker)
    }

    private fun setupViews() {

        vb.rvNewsList.apply {
            adapter = newsListAdapter
            setHasFixedSize(false)
        }

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.getNews(ticker).collectLatest {
                newsListAdapter.updateData(it)
            }
        }
    }

    private fun setupRecyclerViewAdapter() =
        BaseRVAdapter<FragmentNewsItemBinding, NewsItemData>(
            viewHolderInflater = { layoutInflater, parent, attachToParent ->
                FragmentNewsItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            viewHolderBinder = { holder, itemData, _ ->
                with(holder) {

                    if (itemData.image.isNotBlank()) {
                        icImage.isVisible = true
                        icImage.load(itemData.image)
                    } else {
                        icImage.isVisible = false
                    }

                    tvHeader.text = itemData.headline
                    tvDate.text =
                        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(itemData.datetime)
                    tvCategory.text = itemData.category
                    tvSummary.text = itemData.summary
                    tvSource.text = itemData.source

                }
            },
        )
}