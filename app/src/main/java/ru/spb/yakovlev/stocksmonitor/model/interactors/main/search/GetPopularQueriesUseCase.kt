package ru.spb.yakovlev.stocksmonitor.model.interactors.main.search

import ru.spb.yakovlev.stocksmonitor.data.repositories.SearchResultsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPopularQueriesUseCase @Inject constructor(
    private val searchResultsRepository: SearchResultsRepository,
) {
    operator fun invoke(): List<String> =
        searchResultsRepository.popularQueries
}