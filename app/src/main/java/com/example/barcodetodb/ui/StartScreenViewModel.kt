package com.example.barcodetodb.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class StartScreenViewModel: ViewModel() {

    var uiState by mutableStateOf(StartScreenUiState())

    fun changeThemeAndSwitchButtonState(state: Boolean){
        uiState = uiState.copy(
            currentThemeIsDark = !uiState.currentThemeIsDark,
            toggleThemeButton = state
        )
    }
    fun changeDropdownMenuState(){
        uiState = uiState.copy(expandedTopBarMenu = !uiState.expandedTopBarMenu)
    }
}
data class StartScreenUiState(
    val currentThemeIsDark: Boolean = false,
    val expandedTopBarMenu: Boolean = false,
    val toggleThemeButton: Boolean = false
)