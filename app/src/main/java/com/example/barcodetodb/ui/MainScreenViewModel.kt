package com.example.barcodetodb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val OfflineItemsRepository: OfflineItemsRepository
) : ViewModel() {
    private val _itemFlow = MutableStateFlow<List<Item>>(emptyList())
    val itemFlow: StateFlow<List<Item>> get() = _itemFlow
    init {
        viewModelScope.launch {
            OfflineItemsRepository.getAllItemsStream().collect {
                _itemFlow.value = it
            }
        }
    }
    fun deleteItem(item: Item){
        viewModelScope.launch { OfflineItemsRepository.deleteItem(item) }

    }
}