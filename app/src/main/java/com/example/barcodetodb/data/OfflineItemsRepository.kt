package com.example.barcodetodb.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDAO) : ItemsRepository {

    override fun getItemsByNameStream(itemName: String): Flow<List<Item?>> = itemDao.getItemsByName(itemName)

    override fun getItemsByDateStream(itemDate: String): Flow<List<Item?>> = itemDao.getItemsByDate(itemDate)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override suspend fun getCode(item: Item?) = itemDao.getCode(item)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}