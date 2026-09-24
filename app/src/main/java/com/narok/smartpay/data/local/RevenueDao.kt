package com.narok.smartpay.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RevenueDao {
    @Query("SELECT * FROM revenue_items WHERE isFrequent = 1")
    fun getFrequentItems(): Flow<List<RevenueItem>>

    @Query("SELECT * FROM revenue_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<RevenueItem>>

    @Query("SELECT DISTINCT category FROM revenue_items")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: RevenueTransaction)

    @Query("SELECT * FROM transactions WHERE syncStatus = 'PENDING'")
    suspend fun getPendingTransactions(): List<RevenueTransaction>

    @Query("UPDATE transactions SET syncStatus = 'UPLOADED' WHERE transactionId IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE paymentMode = 'CASH' AND syncStatus = 'UPLOADED'")
    fun getCashUploadedTotal(): Flow<Double?>

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE paymentMode = 'CASH' AND syncStatus = 'PENDING'")
    fun getCashPendingTotal(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM transactions WHERE syncStatus = 'PENDING'")
    fun getPendingCount(): Flow<Int>
}
