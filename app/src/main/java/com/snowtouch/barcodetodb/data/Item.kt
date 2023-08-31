package com.snowtouch.barcodetodb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")

data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemCode: String,
    val itemName: String,
    val itemPrice: Double?,
    val itemQuantity: Int?,
    val writeDate: String
)
