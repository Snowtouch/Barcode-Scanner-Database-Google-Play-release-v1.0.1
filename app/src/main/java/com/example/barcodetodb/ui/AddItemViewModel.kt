package com.example.barcodetodb.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
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
    fun resetTextFields(){
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
    ){
        if (!isEdited) {
            val newItem = Item(
                itemCode = code,
                itemName = name,
                writeDate = LocalDate.now().toString(),
                itemPrice = price?.takeIf { it.isNotBlank() }?.toDouble(),
                itemQuantity = quantity?.toInt()
            )
            OfflineItemsRepository.insertItem(newItem)
        }
        else {
            val newItem = Item(
                id = id.toInt(),
                itemCode = code,
                itemName = name,
                writeDate = date,
                itemPrice = price?.takeIf { it.isNotBlank() }?.toDouble(),
                itemQuantity = quantity?.toInt(),

            )
            OfflineItemsRepository.updateItem(newItem)
        }
    }
    fun scanNewCode(context: Context){
        var rawValue: String?
        val scanner = GmsBarcodeScanning.getClient(context)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                rawValue = barcode.rawValue
                updateUiState(itemUiState.itemDetails.copy(code = rawValue.toString()))
            }
            .addOnCanceledListener {  }
            .addOnFailureListener { e -> updateUiState(itemUiState.itemDetails.copy(code = e.toString())) }

    }
}
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)
data class ItemDetails(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val price: String? = "0",
    val quantity: String? = "1",
    val date: String = ""
)