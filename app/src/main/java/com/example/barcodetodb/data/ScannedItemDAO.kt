package com.example.barcodetodb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ScannedItem)

    @Update
    suspend fun update(item: ScannedItem)

    @Delete
    suspend fun delete(item: ScannedItem)

    @Query("SELECT * from scannedItems WHERE scannedCode = :itemCode")
    suspend fun getCode(itemCode: Int): Flow<ScannedItem>

    @Query("SELECT * from scannedItems WHERE itemName = :itemName")
    suspend fun getItemsByName(itemName: String): Flow<List<ScannedItem>>

    @Query("SELECT * from scannedItems WHERE scanDate = :scanDate")
    suspend fun getItemsByDate(scanDate: String): Flow<List<ScannedItem>>
}