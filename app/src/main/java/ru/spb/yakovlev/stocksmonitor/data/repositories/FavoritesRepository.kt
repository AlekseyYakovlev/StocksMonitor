package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.stocksmonitor.data.local.daos.FavoriteDao
import ru.spb.yakovlev.stocksmonitor.data.local.entites.FavoriteEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    fun getFavoritesFlow(): Flow<List<String>> =
        favoriteDao.getFavoritesFlow()

    suspend fun updateFavorites(ticker: String, isFavorite: Boolean) {
        if (isFavorite) favoriteDao.insert(FavoriteEntry(ticker))
        else favoriteDao.delete(FavoriteEntry(ticker))
    }
}