package com.snowtouch.barcodetodb.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.snowtouch.barcodetodb.data.Item
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val offlineItemsRepository: OfflineItemsRepository,
    private val itemListViewModel: ItemListViewModel
) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
    var editItemFlag by mutableStateOf(false)

    fun updateUiState(updatedItemDetails: ItemDetails)
    {
        itemUiState = itemUiState.copy(itemDetails = updatedItemDetails)
    }
    fun checkForDoubledCode(): Boolean {
        val itemCodeToCheck = itemUiState.itemDetails.code

        val hasDuplicateCode = itemListViewModel.itemFlow.value.any { item ->
            item.itemCode == itemCodeToCheck
        }

        return hasDuplicateCode
    }
    fun resetTextFields()
    {
        val newState = ItemDetails()
        updateUiState(
            itemUiState.itemDetails.copy(
                code = newState.code,
                name = newState.name,
                price = newState.price,
                quantity = newState.quantity
            )
        )
    }
    suspend fun saveOrEditItem(
        id: String = itemUiState.itemDetails.id,
        code: String = itemUiState.itemDetails.code,
        name: String = itemUiState.itemDetails.name,
        price: String? = itemUiState.itemDetails.price,
        quantity: String? = itemUiState.itemDetails.quantity,
        date: String = itemUiState.itemDetails.date,
        isEdited: Boolean
    ) {
        if (!isEdited) {
            val newItem = Item(
                itemCode = code,
                itemName = name,
                writeDate = LocalDate.now().toString(),
                itemPrice = price?.takeIf { it.isNotBlank() }?.toDouble(),
                itemQuantity = quantity?.toInt()
            )
            offlineItemsRepository.insertItem(newItem)
            itemListViewModel.refreshDataFromDatabase()
        } else {
            val newItem = Item(
                id = id.toInt(),
                itemCode = code,
                itemName = name,
                writeDate = date,
                itemPrice = price?.takeIf { it.isNotBlank() }?.toDouble(),
                itemQuantity = quantity?.toInt(),
            )
            offlineItemsRepository.updateItem(newItem)
            itemListViewModel.refreshDataFromDatabase()
        }
    }
    fun scanNewCode(context: Context) {
        var rawValue: String?
        val scanner = GmsBarcodeScanning.getClient(context)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                rawValue = barcode.rawValue
                updateUiState(itemUiState.itemDetails.copy(code = rawValue.toString()))
            }
            .addOnCanceledListener {  }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "$exception", Toast.LENGTH_LONG).show()
            }
    }
}
data class ItemUiState( val itemDetails: ItemDetails = ItemDetails() )

data class ItemDetails(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val price: String? = "0",
    val quantity: String? = "1",
    val date: String = ""
)