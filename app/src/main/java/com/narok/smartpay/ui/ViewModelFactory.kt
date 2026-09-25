package com.narok.smartpay.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.narok.smartpay.data.local.AppDatabase
import com.narok.smartpay.data.repository.RevenueRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RevenueViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = RevenueRepository(database.revenueDao())
            @Suppress("UNCHECKED_CAST")
            return RevenueViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
