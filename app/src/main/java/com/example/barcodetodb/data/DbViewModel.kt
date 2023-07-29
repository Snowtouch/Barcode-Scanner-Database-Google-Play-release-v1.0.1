package com.example.barcodetodb.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DbViewModel @Inject constructor(val OfflineItemsRepository: OfflineItemsRepository) : ViewModel() {
    private val _itemsState = MutableStateFlow<List<Item>>(emptyList())
    val itemsState: StateFlow<List<Item>> = _itemsState

    init {
        fetchData()
    }
    private fun fetchData(){
        viewModelScope.launch {
            OfflineItemsRepository.getAllItemsStream().collect { items ->
                _itemsState.value = items
            }
        }
    }
    fun addItem(item: Item){
        viewModelScope.launch {
            OfflineItemsRepository.insertItem(item)
        }
    }
    fun deleteItem(item: Item){
        viewModelScope.launch {
            OfflineItemsRepository.deleteItem(item)
        }
    }
    fun updateItem(item: Item){
        viewModelScope.launch {
            OfflineItemsRepository.updateItem(item)
        }
    }
    fun getAllItems(){
        viewModelScope.launch {
            OfflineItemsRepository.getAllItemsStream()
        }
    }
    fun getItemByCode(code: String){
        viewModelScope.launch {
            OfflineItemsRepository.getItemCode(code)
        }
    }
    fun getAllItemsByName(name: String){
        viewModelScope.launch {
            OfflineItemsRepository.getItemsByNameStream(name)
        }
    }
    fun getAllItemsByDate(date: String){
        viewModelScope.launch {
            OfflineItemsRepository.getItemsByDateStream(date)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}