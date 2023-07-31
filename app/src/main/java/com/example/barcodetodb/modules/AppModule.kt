package com.example.barcodetodb.modules

import android.content.Context
import androidx.room.Room
import com.example.barcodetodb.data.ItemDAO
import com.example.barcodetodb.data.ItemDAOImpl
import com.example.barcodetodb.data.ItemsDatabase
import com.example.barcodetodb.data.OfflineItemsRepository
import com.example.barcodetodb.ui.AddItemViewModel
import com.example.barcodetodb.ui.MainScreenViewModel
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
        OfflineItemsRepository: OfflineItemsRepository
    ): AddItemViewModel {
        return AddItemViewModel(OfflineItemsRepository)
    }

    @Provides
    @Singleton
    fun provideMainScreenViewModel(
        OfflineItemsRepository: OfflineItemsRepository
    ): MainScreenViewModel {
        return MainScreenViewModel(OfflineItemsRepository)
    }


}