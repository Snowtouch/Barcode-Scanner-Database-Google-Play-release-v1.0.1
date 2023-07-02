package com.example.barcodetodb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scannedItems")

data class ScannedItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scannedCode: Int,
    val itemName: String,
    val scanDate: String,
    val itemPrice: Double? = null
)
