package com.narok.smartpay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "revenue_items")
data class RevenueItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val category: String,          // e.g., "Bus Park", "Cattle Cess", "Market Stall Rent"
    val subType: String,           // e.g., "Trailer Per Day", "Canter / Lorry Between 3 and 7 Tonnes"
    val unitPrice: Double,         // e.g., 200.0, 100.0
    val billingPeriod: String,     // e.g., "Per Day"
    val isFrequent: Boolean = false
)
