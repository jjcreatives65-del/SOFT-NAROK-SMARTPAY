package com.narok.smartpay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "revenue_items")
data class RevenueItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val subGroup: String,          // e.g., "Bus Park", "Cattle Cess", "Street & Taxis"
    val stream: String,            // e.g., "Trailer Per Day", "Hand Cart"
    val unitPrice: Double,         // e.g., 200.0, 50.0
    val isFrequent: Boolean = false
)
