package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateNewsForTickerUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(ticker: String) =
        newsRepository.loadActualNews(ticker)
}