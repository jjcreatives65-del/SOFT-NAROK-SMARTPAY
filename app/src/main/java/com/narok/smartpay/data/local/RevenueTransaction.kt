package com.narok.smartpay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transactions")
data class RevenueTransaction(
    @PrimaryKey val receiptNo: String = UUID.randomUUID().toString(), // Used as receipt no & QR code
    val stream: String,            // e.g., "Hand Cart"
    val subGroup: String,          // e.g., "Street & Taxis"
    val narration: String,         // e.g., "KDE 150L"
    val unitPrice: Double,
    val quantity: Int,
    val totalAmount: Double,
    val paymentMode: String,       // "CASH" or "MOBILE_MONEY"
    val agentName: String,         // e.g., "WILSON MASIKONTE"
    val section: String,           // e.g., "Narok Central"
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING" // "PENDING" or "UPLOADED"
)
