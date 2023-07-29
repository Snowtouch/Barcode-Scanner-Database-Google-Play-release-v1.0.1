package com.example.barcodetodb.data


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineItemsRepository @Inject constructor(private val itemDao: ItemDAO) : ItemsRepository {

    override fun getItemsByNameStream(itemName: String): Flow<List<Item?>> = itemDao.getItemsByName(itemName)

    override fun getItemsByDateStream(itemDate: String): Flow<List<Item?>> = itemDao.getItemsByDate(itemDate)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemCode(itemCode: String): Flow<Item> = itemDao.getItemCode(itemCode)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}