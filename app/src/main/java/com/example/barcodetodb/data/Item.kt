package com.example.barcodetodb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")

data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemCode: Int,
    val itemName: String,
    val itemQuantity: Int? = 0,
    val writeDate: String
)
