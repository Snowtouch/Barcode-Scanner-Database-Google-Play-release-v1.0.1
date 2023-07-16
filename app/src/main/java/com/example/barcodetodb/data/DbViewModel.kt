package com.example.barcodetodb.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DbViewModel(context: Context) : ViewModel() {
    private val repository: OfflineItemsRepository
    private val _itemsState = MutableStateFlow<List<Item>>(emptyList())
    val itemsState: StateFlow<List<Item>> = _itemsState

    init {
        val itemDAO = ItemsDatabase.getDatabase(context).itemDAO()
        repository = OfflineItemsRepository(itemDAO)
        fetchData()
    }
    private fun fetchData(){
        viewModelScope.launch {
            repository.getAllItemsStream().collect { items ->
                _itemsState.value = items
            }
        }
    }
    fun addItem(item: Item){
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }
    fun deleteItem(item: Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
    fun updateItem(item: Item){
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }
    fun getAllItems(){
        viewModelScope.launch {
            repository.getAllItemsStream()
        }
    }
    fun getItemByCode(code: String){
        viewModelScope.launch {
            repository.getItemCode(code)
        }
    }
    fun getAllItemsByName(name: String){
        viewModelScope.launch {
            repository.getItemsByNameStream(name)
        }
    }
    fun getAllItemsByDate(date: String){
        viewModelScope.launch {
            repository.getItemsByDateStream(date)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}