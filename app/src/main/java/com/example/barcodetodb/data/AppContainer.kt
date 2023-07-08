package com.example.barcodetodb.data

import android.content.Context

interface AppContainer {
    val itemsRepository: ItemsRepository
}
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(ItemsDatabase.getDatabase(context).itemDAO())
    }}