package com.example.barcodetodb.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Snackbar(
    private val snackbarScope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState

) {
    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short){
        snackbarScope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }
}