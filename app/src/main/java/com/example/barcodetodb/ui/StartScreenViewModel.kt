package com.example.barcodetodb.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StartScreenViewModel: ViewModel() {
    val currentThemeIsDark = mutableStateOf(false)

    fun changeTheme(){
        currentThemeIsDark.value = !currentThemeIsDark.value
    }
}