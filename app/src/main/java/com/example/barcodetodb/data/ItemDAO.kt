package com.example.barcodetodb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from Items")
    fun getAllItems(): Flow<List<Item>>

    @Query("DELETE from Items")
    fun deleteAllItems()

    @Query("SELECT * from Items WHERE itemCode = :itemCode")
    fun getItemCode(itemCode: String): Flow<Item>

    @Query("SELECT * from Items WHERE itemName = :itemName")
    fun getItemsByName(itemName: String): Flow<List<Item>>

    @Query("SELECT * from Items WHERE writeDate = :writeDate")
    fun getItemsByDate(writeDate: String): Flow<List<Item>>
}