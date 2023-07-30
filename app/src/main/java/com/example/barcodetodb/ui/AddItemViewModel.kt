package com.example.barcodetodb.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val OfflineItemsRepository: OfflineItemsRepository
    ) : ViewModel()
{

    var itemUiState by mutableStateOf(ItemUiState())

    fun updateUiState(updatedItemDetails: ItemDetails){
        itemUiState = itemUiState.copy(itemDetails = updatedItemDetails)
    }

    suspend fun saveNewItem(
        code: String = itemUiState.itemDetails.code,
        name: String = itemUiState.itemDetails.name,
        price: String? = itemUiState.itemDetails.price,
        quantity: String? = itemUiState.itemDetails.quantity
    ){
        val newItem = Item(
            itemCode = code,
            itemName = name,
            writeDate = LocalDate.now().toString(),
            itemPrice = price?.takeIf { it.isNotBlank() }?.toDouble(),
            itemQuantity = quantity?.toInt())
        OfflineItemsRepository.insertItem(newItem)
    }
}
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)
data class ItemDetails(
    val code: String = "",
    val name: String = "",
    val price: String? = "0",
    val quantity: String? = "1",
)