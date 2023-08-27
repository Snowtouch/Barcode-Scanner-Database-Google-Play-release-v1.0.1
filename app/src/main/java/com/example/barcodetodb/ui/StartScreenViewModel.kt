package com.example.barcodetodb.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StartScreenViewModel: ViewModel() {

    var uiState by mutableStateOf(StartScreenViewState())

    fun changeThemeAndSwitchButtonState(state: Boolean) {
        uiState = uiState.copy(
            currentThemeIsDark = !uiState.currentThemeIsDark,
            toggleThemeButton = state
        )
    }
    fun changeDropdownMenuState() {
        uiState = uiState.copy(expandedTopBarMenu = !uiState.expandedTopBarMenu)
    }
}
data class StartScreenViewState(
    val currentThemeIsDark: Boolean = false,
    val expandedTopBarMenu: Boolean = false,
    val toggleThemeButton: Boolean = false,
)