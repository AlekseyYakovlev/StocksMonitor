package ru.spb.yakovlev.stocksmonitor.model.interactors

import ru.spb.yakovlev.stocksmonitor.data.repositories.FavoritesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateFavoriteStatusUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(ticker: String, isFavorite: Boolean) =
        favoritesRepository.updateFavorites(ticker, isFavorite)
}