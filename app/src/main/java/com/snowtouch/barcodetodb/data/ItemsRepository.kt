package com.snowtouch.barcodetodb.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>

    fun deleteAllItems()

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)
}