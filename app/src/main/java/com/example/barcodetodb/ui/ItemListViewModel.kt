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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
    private fun dateToEpochMilis(date: String): Long {
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
        val date = LocalDate.parse(date, formatter)
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
    fun filterList(
        query: String = "",
        startDate: Long?,
        endDate: Long?
    ) {
        val filteredItems = _rawItemFlow.value.filter { item ->
            val queryMatches = item.itemCode.contains(query, ignoreCase = true) ||
                    item.itemName.contains(query, ignoreCase = true)

            val itemDate = dateToEpochMilis(item.writeDate)

            val startDateMatches = startDate == null || (itemDate >= startDate)
            val endDateMatches = endDate == null || (itemDate <= endDate)

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