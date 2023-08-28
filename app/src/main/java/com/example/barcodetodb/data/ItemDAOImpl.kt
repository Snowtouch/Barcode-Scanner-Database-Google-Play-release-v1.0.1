package com.example.barcodetodb.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemDAOImpl @Inject constructor(private val itemsDatabase: ItemsDatabase) : ItemDAO {

    override suspend fun insert(item: Item) {
        itemsDatabase.itemDAO().insert(item)
    }

    override fun deleteAllItems(){
        itemsDatabase.itemDAO().deleteAllItems()
    }
    override suspend fun update(item: Item) {
        itemsDatabase.itemDAO().update(item)
    }

    override suspend fun delete(item: Item) {
        itemsDatabase.itemDAO().delete(item)
    }

    override fun getAllItems(): Flow<List<Item>> {
        return itemsDatabase.itemDAO().getAllItems()
    }

    override fun getItemCode(itemCode: String): Flow<Item> {
        return itemsDatabase.itemDAO().getItemCode(itemCode)
    }

    override fun getItemsByName(itemName: String): Flow<List<Item>> {
        return itemsDatabase.itemDAO().getItemsByName(itemName)
    }

    override fun getItemsByDate(writeDate: String): Flow<List<Item>> {
        return itemsDatabase.itemDAO().getItemsByDate(writeDate)
    }
}
