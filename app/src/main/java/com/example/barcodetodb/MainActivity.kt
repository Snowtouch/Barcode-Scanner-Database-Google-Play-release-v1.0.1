package com.example.barcodetodb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.barcodetodb.data.ItemsDatabase
import com.example.barcodetodb.ui.BarcodeApp
import com.example.barcodetodb.ui.ItemListViewModel
import com.example.barcodetodb.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var itemListViewModel: ItemListViewModel
    private lateinit var itemsDatabase: ItemsDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemsDatabase = ItemsDatabase.getDatabase(this)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BarcodeApp(activity = this@MainActivity)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        itemListViewModel.refreshDataFromDatabase()
    }
}
@Preview
@Composable
fun BarcodeAppPreview(){
    BarcodeApp(activity = MainActivity())
}