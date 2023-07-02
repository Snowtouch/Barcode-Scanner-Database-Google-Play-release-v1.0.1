package com.example.barcodetodb.data

data class AppUiState(
    val currentScannedCode: Int = 0,
    val currentScannedName: String = " ",
    val currentScanDate: String = " "
)
