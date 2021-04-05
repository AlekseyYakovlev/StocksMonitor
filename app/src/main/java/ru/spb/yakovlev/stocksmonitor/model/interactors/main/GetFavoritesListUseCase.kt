package ru.spb.yakovlev.stocksmonitor.model.interactors.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.repositories.CompaniesRepository
import ru.spb.yakovlev.stocksmonitor.data.repositories.FavoritesRepository
import ru.spb.yakovlev.stocksmonitor.model.Company
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoritesListUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val companyRepository: CompaniesRepository,
) {
    suspend operator fun invoke(): Flow<List<Company>> =
        favoritesRepository.getFavoritesFlow().map { ticker ->
            Timber.tag("1234567").d("GetFavoritesListUseCase map")
            companyRepository.getCompaniesByTickers(ticker)
        }
}