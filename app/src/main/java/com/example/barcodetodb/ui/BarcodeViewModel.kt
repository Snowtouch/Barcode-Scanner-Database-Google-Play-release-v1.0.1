package com.example.barcodetodb.ui

import android.content.Context
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.barcodetodb.data.AppUiState
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.ItemsDatabase
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class BarcodeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    //val itemDAO: ItemDAO = data.ItemDAO
    var enteredCode by mutableStateOf(" ")
        private set
    var enteredName by mutableStateOf(" ")
        private set

    fun enterNewItemCode(newItemCode: String){
        enteredCode = newItemCode
    }

    fun enterNewItemName(newItemName: String){
        enteredName = newItemName
    }
    suspend fun saveNewItem(code: Int, name: String){
        val item = Item(itemCode = code, itemName = name, writeDate = LocalDate.now().toString())
        ItemsDatabase.getDatabase().itemDAO().getAllItems()
    }
    }
