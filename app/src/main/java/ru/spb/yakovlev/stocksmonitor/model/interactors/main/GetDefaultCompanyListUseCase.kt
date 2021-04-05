package ru.spb.yakovlev.stocksmonitor.model.interactors.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.repositories.CompaniesRepository
import ru.spb.yakovlev.stocksmonitor.data.repositories.IndicesRepository
import ru.spb.yakovlev.stocksmonitor.model.Company
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_INDEX = "^DJI"

@Singleton
class GetDefaultCompanyListUseCase @Inject constructor(
    private val indicesRepository: IndicesRepository,
    private val companyRepository: CompaniesRepository,
) {
  suspend operator fun invoke(): Flow<List<Company>> =
        indicesRepository.getIndexFlow(DEFAULT_INDEX).map { index ->
            Timber.tag("1234567").d("GetDefaultCompanyListUseCase map")
            companyRepository.getCompaniesByTickers(index.constituents)
                .dropLast(5) //Not to catch api error "Limit exceed"
        }
}