package com.example.barcodetodb.ui

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Snackbar(
    val snackbarScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState

) {
    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short){
        snackbarScope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }
}