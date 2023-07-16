package com.example.barcodetodb.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.barcodetodb.data.AppUiState
import com.example.barcodetodb.data.DbViewModel
import com.example.barcodetodb.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class BarcodeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val _dataFlow = MutableStateFlow<List<Item>>(emptyList())
    val dataFlow: StateFlow<List<Item>> = _dataFlow

    var enteredCode by mutableStateOf(" ")
        private set
    var enteredName by mutableStateOf(" ")
        private set
    var enteredQuantity by mutableStateOf("1")

    fun observeDataFromDbViewModel(dbViewModel: DbViewModel){
        dbViewModel.itemsState
    }

    fun newItemCode(newItemCode: String){
        enteredCode = newItemCode
    }
    fun newItemName(newItemName: String){
        enteredName = newItemName
    }
    fun newItemQuantity(newItemQuantity: String){
        enteredQuantity = newItemQuantity
    }
    fun saveNewItem(
        dbViewModel: DbViewModel,
        code: Int,
        name: String,
        quantity: String? = enteredQuantity
    ){
        val item = Item(itemCode = code, itemName = name, writeDate = LocalDate.now().toString(),
            itemQuantity = quantity?.toInt())
        dbViewModel.addItem(item)
    }
}
