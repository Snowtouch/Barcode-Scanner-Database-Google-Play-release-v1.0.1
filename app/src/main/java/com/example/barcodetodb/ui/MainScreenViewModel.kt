package com.example.barcodetodb.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val OfflineItemsRepository: OfflineItemsRepository
) : ViewModel(){
    private val _itemFlow = MutableStateFlow<List<Item>>(emptyList())
    val itemFlow: StateFlow<List<Item>> get() = _itemFlow

    init {
        viewModelScope.launch {
            OfflineItemsRepository.getAllItemsStream().collect {
                _itemFlow.value = it
            }
        }
    }
}