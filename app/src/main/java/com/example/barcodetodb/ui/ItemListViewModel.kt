package com.example.barcodetodb.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodetodb.data.Item
import com.example.barcodetodb.data.OfflineItemsRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
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

    companion object {
        const val REQUEST_SAVE_JSON = 123
    }
    init {
        refreshDataFromDatabase()
    }
    private fun dateToEpochMilis(date: String): Long {
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
        val localDate = LocalDate.parse(date, formatter)
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
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
    fun saveDatabaseToFile(context: Context) {
        val itemsDb: List<Item> = rawItemFlow.value
        val gson = Gson()
        try {
            val json = gson.toJson(itemsDb)
            val file = createDatabaseFile(context, json)

            Toast.makeText(
                context,
                "Database saved successfully to ${file.absolutePath}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            showErrorMessage(context, "Error saving database: ${e.message}")
        }
    }

    fun sendDatabaseByMail(context: Context) {
        val itemsDb: List<Item> = rawItemFlow.value
        val gson = Gson()
        try {
            val json = gson.toJson(itemsDb)
            val file = createDatabaseFile(context, json)
            val fileUri = FileProvider.getUriForFile(context,
                "com.example.barcodetodb.fileprovider", file)

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "application/json"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Database Backup")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Backup of database attached.")
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(Intent.createChooser(emailIntent, "Send email"))
        } catch (e: Exception) {
            showErrorMessage(context, "Error sending email: ${e.message}")
        }
    }

    private fun createDatabaseFile(context: Context, json: String): File {
        val folder = context.getExternalFilesDir(null)
            ?: throw IllegalStateException("External files directory is null")

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val fileName = "items_database ${LocalDate.now()}.json"
        val file = File(folder, fileName)
        file.writeText(json)
        return file
    }

    private fun showErrorMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
