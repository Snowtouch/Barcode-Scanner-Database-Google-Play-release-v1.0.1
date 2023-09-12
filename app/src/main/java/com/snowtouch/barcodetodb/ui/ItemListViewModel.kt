package com.snowtouch.barcodetodb.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowtouch.barcodetodb.R
import com.snowtouch.barcodetodb.data.Item
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(private val offlineItemsRepository: OfflineItemsRepository
) : ViewModel() {
    //Item list raw and filtered flows
    private val _rawItemFlow = MutableStateFlow<List<Item>>(emptyList())
    private val _filteredItemFlow = MutableStateFlow<List<Item>>(emptyList())
    val itemFlow: StateFlow<List<Item>> = _rawItemFlow
    val filteredItemFlow: StateFlow<List<Item>> = _filteredItemFlow

    var isFiltered = mutableStateOf(false)

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
        val filteredItemsFlow = _rawItemFlow.map { rawItems ->
            rawItems.filter { item ->
                val queryMatches = item.itemCode.contains(query, ignoreCase = true) ||
                        item.itemName.contains(query, ignoreCase = true)

                val itemDate = dateToEpochMilis(item.writeDate)

                val startDateMatches = startDate == null || (itemDate >= startDate)
                val endDateMatches = endDate == null || (itemDate <= endDate)

                queryMatches && startDateMatches && endDateMatches
            }
        }
        viewModelScope.launch {
            _filteredItemFlow.value = filteredItemsFlow.first()
        }
    }
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            offlineItemsRepository.deleteItem(item)
            refreshDataFromDatabase()
        }
    }
    suspend fun importDatabaseFromFileUri(context: Context, fileUri: Uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(fileUri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.readText()

            withContext(Dispatchers.IO) {
                reader.close()
            }
            val items: List<Item> = Gson().fromJson(json, Array<Item>::class.java).toList()

            withContext(Dispatchers.IO) {
                offlineItemsRepository.deleteAllItems()
                for (item in items) offlineItemsRepository.insertItem(item)
            }

            refreshDataFromDatabase()

        } catch (e: Exception) { showErrorMessage(context, e.message.toString()) }
    }

    fun refreshDataFromDatabase() {
        viewModelScope.launch {
            _rawItemFlow.value = offlineItemsRepository.getAllItemsStream().first()
        }
    }
    fun saveDatabaseToFile(context: Context) {
        val gson = Gson()
        try {
            val json = gson.toJson(_rawItemFlow.value)
            val file = createDatabaseFile(context, json)

            Toast.makeText(
                context,
                context.getString(R.string.database_saved_successfully_message, file.absolutePath),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            showErrorMessage(context,
                context.getString(R.string.error_saving_database_message, e.message))
        }
    }
    fun sendDatabaseByMail(context: Context) {
        val itemsDb: List<Item> = _rawItemFlow.value
        val gson = Gson()
        try {
            val json = gson.toJson(itemsDb)
            val file = createDatabaseFile(context, json)
            val fileUri = FileProvider.getUriForFile(context,
                "com.snowtouch.barcodetodb.fileprovider", file)

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "application/json"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                context.getString(R.string.sending_db_message_title))
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                context.getString(R.string.sending_db_message_text))
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(Intent.createChooser(emailIntent,
                context.getString(R.string.send_email)))
        } catch (e: Exception) {
            showErrorMessage(context, context.getString(R.string.error_sending_email, e.message))
        }
    }
    private fun createDatabaseFile(context: Context, json: String): File {
        val folder = context.getExternalFilesDir(null)
            ?: throw IllegalStateException(context.getString(R.string.external_files_directory_is_null))
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val fileName = "items_database ${LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))}.json"
        val file = File(folder, fileName)
        file.writeText(json)
        return file
    }
    private fun showErrorMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
