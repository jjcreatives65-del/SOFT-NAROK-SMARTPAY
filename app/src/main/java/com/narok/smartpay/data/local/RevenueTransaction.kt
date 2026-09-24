package com.narok.smartpay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transactions")
data class RevenueTransaction(
    @PrimaryKey val transactionId: String = UUID.randomUUID().toString(),
    val itemId: String,
    val itemName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalAmount: Double,
    val paymentMode: String,       // "CASH" or "MOBILE_MONEY"
    val agentName: String,         // e.g., "Wilson Masikonte"
    val zone: String,              // e.g., "Narok Central"
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING" // "PENDING" or "UPLOADED"
)
