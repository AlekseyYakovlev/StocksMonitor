package ru.spb.yakovlev.stocksmonitor.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.local.entites.PrefEntry

@Dao
interface PrefsDao : BaseDao<PrefEntry> {
    @Query(
        """
            SELECT value 
            FROM PrefEntry
            WHERE name = :name            
            LIMIT 1
        """
    )
    suspend fun getValue(name: String): String?

    @Query(
        """
            SELECT listValue 
            FROM PrefEntry
            WHERE name = :name            
            LIMIT 1
        """
    )
    suspend fun getListValueInner(name: String): String?

    suspend fun getListValue(name: String): List<String> =
        getListValueInner(name)?.split(";") ?: emptyList()


    @Query(
        """
            SELECT value 
            FROM PrefEntry
            WHERE name = :name            
            LIMIT 1
        """
    )
    fun getValueFlow(name: String): Flow<String?>

    @Query(
        """
            SELECT listValue 
            FROM PrefEntry
            WHERE name = :name            
            LIMIT 1
        """
    )
    fun getListValueFlowInner(name: String): Flow<String?>

    fun getListValueFlow(name: String): Flow<List<String>> =
        getListValueFlowInner(name).map { it?.split(";") ?: emptyList() }


    suspend fun save(name: String, value: String) {
        insert(PrefEntry(name, value))
    }

    suspend fun saveList(name: String, listValue: List<String>) {
        insert(PrefEntry(name, listValue = listValue))
    }
}