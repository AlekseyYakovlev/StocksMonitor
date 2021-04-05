package ru.spb.yakovlev.stocksmonitor.model.interactors.main.search

import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.repositories.SearchResultsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetResentQueriesUseCase @Inject constructor(
    private val searchResultsRepository: SearchResultsRepository,
) {
    operator fun invoke(): Flow<List<String>> =
        searchResultsRepository.resentQueriesFlow
}