package com.snowtouch.barcodetodb.ui

import com.snowtouch.barcodetodb.data.Item
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith
class ItemListViewModelTest {

    private lateinit var offlineItemsRepository: OfflineItemsRepository
    private lateinit var viewModel: ItemListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        offlineItemsRepository = mockk()
    }
    @AfterEach
    fun dispose(){
        Dispatchers.resetMain()
    }

    @Test
    fun filterListTest() = runBlocking {
        // Given
        val startDate = 1640995200000L // 01-01-2022
        val endDate = 1641081600000L // 02-01-2022

        val items = listOf(
            Item(
                id = 1,
                itemCode = "123456",
                itemName = "Boots",
                itemPrice = null,
                itemQuantity = null,
                writeDate = "2021-12-31"),  // Date out of range
            Item(
                id = 2,
                itemCode = "43342342",
                itemName = "Cape",
                itemPrice = 0.0,
                itemQuantity = 3,
                writeDate = "2022-01-01"),  // Date in range
            Item(
                id = 3,
                itemName = "Boat",
                itemCode = "37823634",
                itemPrice = 1.2,
                itemQuantity = 4,
                writeDate = "2022-01-02"),  // Date in range
            Item(
                id = 4,
                itemCode = "02873",
                itemName = "Car",
                itemPrice = 10000.0,
                itemQuantity = 1,
                writeDate = "2022-01-03")  // Date out of range
        )

        // Mock the behavior of offlineItemsRepository
        coEvery { offlineItemsRepository.getAllItemsStream() } coAnswers {
            flowOf(items)
        }
        viewModel = ItemListViewModel(offlineItemsRepository)
        viewModel.refreshDataFromDatabase()

        // When
        viewModel.filterList(startDate = startDate, endDate = endDate)

        // Then
        val filteredItems = viewModel.filteredItemFlow.value
        assertEquals(2, filteredItems.size)
        assertEquals(2, filteredItems[0].id)
        assertEquals(3, filteredItems[1].id)
    }
}