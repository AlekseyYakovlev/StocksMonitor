package ru.spb.yakovlev.stocksmonitor.model.interactors

import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.repositories.CompaniesRepository
import ru.spb.yakovlev.stocksmonitor.model.Company
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCompanyByTickerUseCase @Inject constructor(
    private val companiesRepository: CompaniesRepository,
) {
    operator fun invoke(ticker: String): Flow<Company> =
        companiesRepository.getCompanyByTickerFlow(ticker)
}