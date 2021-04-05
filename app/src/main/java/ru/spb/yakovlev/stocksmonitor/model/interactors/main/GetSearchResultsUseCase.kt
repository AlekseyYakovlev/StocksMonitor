package ru.spb.yakovlev.stocksmonitor.model.interactors.main

import ru.spb.yakovlev.stocksmonitor.data.repositories.CompaniesRepository
import ru.spb.yakovlev.stocksmonitor.data.repositories.SearchResultsRepository
import ru.spb.yakovlev.stocksmonitor.model.Company
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchResultsUseCase @Inject constructor(
    private val searchResultsRepository: SearchResultsRepository,
    private val companyRepository: CompaniesRepository,
) {
    suspend operator fun invoke(query: String, listSize:Int): List<Company> {
        if (query.isBlank()) return emptyList()

        val foundTickers = searchResultsRepository.findTickers(query).take(listSize)
        return companyRepository.getCompaniesByTickers(foundTickers)
    }
}