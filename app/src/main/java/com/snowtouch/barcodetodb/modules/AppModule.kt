package com.snowtouch.barcodetodb.modules

import android.content.Context
import androidx.room.Room
import com.snowtouch.barcodetodb.data.ItemDAO
import com.snowtouch.barcodetodb.data.ItemDAOImpl
import com.snowtouch.barcodetodb.data.ItemsDatabase
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import com.snowtouch.barcodetodb.ui.AddItemViewModel
import com.snowtouch.barcodetodb.ui.ItemListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideItemDAO(itemsDatabase: ItemsDatabase): ItemDAO {
        return ItemDAOImpl(itemsDatabase)
    }
    @Provides
    @Singleton
    fun provideItemsDatabase(@ApplicationContext context: Context): ItemsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ItemsDatabase::class.java,
            "items_database"
        ).build()
    }
    @Provides
    fun provideOfflineItemsRepository(itemDao: ItemDAO): OfflineItemsRepository {
        return OfflineItemsRepository(itemDao)
    }
    @Provides
    @Singleton
    fun provideAddItemViewModel(
        offlineItemsRepository: OfflineItemsRepository
    ): AddItemViewModel {
        return AddItemViewModel(offlineItemsRepository, ItemListViewModel(offlineItemsRepository))
    }

    @Provides
    @Singleton
    fun provideItemListViewModel(
        offlineItemsRepository: OfflineItemsRepository
    ): ItemListViewModel {
        return ItemListViewModel(offlineItemsRepository)
    }


}