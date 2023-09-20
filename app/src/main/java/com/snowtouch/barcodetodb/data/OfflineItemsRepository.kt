package com.snowtouch.barcodetodb.data


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineItemsRepository @Inject constructor(private val itemDao: ItemDAO) : ItemsRepository {

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun deleteAllItems() = itemDao.deleteAllItems()

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}