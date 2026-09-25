package com.narok.smartpay.data

import android.content.Context
import com.narok.smartpay.data.local.AppDatabase
import com.narok.smartpay.data.local.RevenueItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Seeder {
    fun seedInitialData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(context).revenueDao()
            if (dao.getFrequentItems().toString().isEmpty()) { // Simplistic check
                return@launch
            }
            
            // Just pushing default items for demonstration
            val defaultItems = listOf(
                RevenueItem(subGroup = "Street & Taxis", stream = "Trailer Per Day", unitPrice = 200.0, isFrequent = true),
                RevenueItem(subGroup = "Street & Taxis", stream = "Canter / Lorry Between 3 and 7 Tonnes", unitPrice = 100.0, isFrequent = false),
                RevenueItem(subGroup = "Street & Taxis", stream = "Lorry More Than 7 Tonnes", unitPrice = 150.0, isFrequent = false),
                RevenueItem(subGroup = "Street & Taxis", stream = "Small Car / 1 Ton Pick-Up and Below", unitPrice = 100.0, isFrequent = false),
                RevenueItem(subGroup = "Street & Taxis", stream = "Hand Cart", unitPrice = 50.0, isFrequent = true),
                RevenueItem(subGroup = "Bus park", stream = "Bus park Kiosk", unitPrice = 50.0, isFrequent = true),
                RevenueItem(subGroup = "Cattle cess", stream = "Cow / Bull", unitPrice = 100.0, isFrequent = true),
                RevenueItem(subGroup = "Cattle cess", stream = "Sheep & Goat", unitPrice = 50.0, isFrequent = false)
            )

            // A real app would check if items already exist properly
            // Here we assume if there are no subgroups, it's empty.
            defaultItems.forEach { 
                dao.insertRevenueItem(it)
            }
        }
    }
}