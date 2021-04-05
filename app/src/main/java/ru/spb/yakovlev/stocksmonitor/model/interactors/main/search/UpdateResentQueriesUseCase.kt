package ru.spb.yakovlev.stocksmonitor.model.interactors.main.search

import ru.spb.yakovlev.stocksmonitor.data.repositories.SearchResultsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateResentQueriesUseCase @Inject constructor(
    private val searchResultsRepository: SearchResultsRepository,
) {
    suspend operator fun invoke(query: String) =
        searchResultsRepository.updateResentQueries(query)
}