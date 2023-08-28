package com.example.barcodetodb.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>

    fun deleteAllItems()
    fun getItemCode(itemCode: String): Flow<Item>

    fun getItemsByNameStream(itemName: String): Flow<List<Item?>>

    fun getItemsByDateStream(itemDate: String): Flow<List<Item?>>

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)
}