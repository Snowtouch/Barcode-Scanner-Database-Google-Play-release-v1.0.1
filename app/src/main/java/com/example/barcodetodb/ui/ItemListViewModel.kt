package com.example.barcodetodb.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(private val offlineItemsRepository: OfflineItemsRepository
) : ViewModel() {
    //Item list raw and filtered flows
    private val _rawItemFlow = MutableStateFlow<List<Item>>(emptyList())
    val rawItemFlow: StateFlow<List<Item>> get() = _rawItemFlow

    private val _filteredItemFlow = MutableStateFlow<List<Item>>(emptyList())
    val filteredItemFlow: StateFlow<List<Item>> = _filteredItemFlow

    var isFiltered = mutableStateOf(false)

    init {
        refreshDataFromDatabase()
    }
    fun filterList(
        query: String = "",
        startDate: Long?,
        endDate: Long?
    ) {
        val filteredItems = _rawItemFlow.value.filter { item ->
            val queryMatches = item.itemCode.contains(query, ignoreCase = true) ||
                    item.itemName.contains(query, ignoreCase = true)
            val itemDateMilis = Instant.parse(item.writeDate)
            val itemLong = itemDateMilis.toEpochMilli()
            val startDateMatches = startDate == null || itemLong >= startDate
            val endDateMatches = endDate == null || itemLong <= endDate

            queryMatches && startDateMatches && endDateMatches
        }
        _filteredItemFlow.value = filteredItems
    }
    fun deleteItem(item: Item){
        viewModelScope.launch { offlineItemsRepository.deleteItem(item) }

    }
    fun refreshDataFromDatabase() {
        viewModelScope.launch {
            offlineItemsRepository.getAllItemsStream().collect {
                _rawItemFlow.value = it
            }
        }
    }
}