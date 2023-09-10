package com.snowtouch.barcodetodb.ui

import com.snowtouch.barcodetodb.data.Item
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ItemListViewModelTest {

    @Mock
    lateinit var offlineItemsRepository: OfflineItemsRepository
    private lateinit var viewModel: ItemListViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ItemListViewModel(offlineItemsRepository)
    }
    @Test
    fun filterListTest() = runBlocking {
        //Given
        val startDate = 1640995200000L // 31-12-2021
        val endDate = 1641081600000L // 01-01-2022
        val itemName1 = ""
        val itemName2 = "Boots"
        val itemName3 = "ca"
        val itemCode1 = "34"
        val itemCode2 = "99999"
        val itemCode3 = ""

        val items = listOf(
            Item(
                id = 1,
                itemCode = "123456",
                itemName = "Boots",
                itemPrice = null,
                itemQuantity = null,
                writeDate = "2021-12-30"),  // Date out of range
            Item(
                id = 2,
                itemCode = "43342342",
                itemName = "Cape",
                itemPrice = 0.0,
                itemQuantity = 3,
                writeDate = "2021-12-31"),  // Date in range
            Item(
                id = 3,
                itemName = "Boat",
                itemCode = "37823634",
                itemPrice = 1.2,
                itemQuantity = 4,
                writeDate = "2022-01-01"),  // Date in range
            Item(
                id = 4,
                itemCode = "02873",
                itemName = "Car",
                itemPrice = 10000.0,
                itemQuantity = 1,
                writeDate = "2022-01-02")  // Date out of range
        )

        whenever(offlineItemsRepository.getAllItemsStream()).thenReturn(flow { emit(items) })

        //When
        viewModel.filterList(startDate = startDate, endDate = endDate)

        //Then
        val filteredItems = viewModel.filteredItemFlow.value
        assertEquals(2, filteredItems.size) // Oczekujemy, że tylko dwa elementy zostaną zachowane w wyniku filtrowania
        assertEquals(2, filteredItems[0].id)
        assertEquals(3, filteredItems[1].id)
    }

}