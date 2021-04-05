package ru.spb.yakovlev.stocksmonitor.model.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.repositories.FavoritesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteStatusUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {
    operator fun invoke(ticker: String): Flow<Boolean> =
        favoritesRepository.getFavoritesFlow().map { list ->
            list.contains(ticker)
        }
}