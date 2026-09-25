package com.narok.smartpay.data.repository

import com.narok.smartpay.data.local.RevenueDao
import com.narok.smartpay.data.local.RevenueItem
import com.narok.smartpay.data.local.RevenueTransaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class RevenueRepository(private val dao: RevenueDao) {

    fun getFrequentItems(): Flow<List<RevenueItem>> = dao.getFrequentItems()

    fun getItemsBySubGroup(subGroup: String): Flow<List<RevenueItem>> = dao.getItemsBySubGroup(subGroup)

    fun getAllSubGroups(): Flow<List<String>> = dao.getAllSubGroups()

    suspend fun insertTransaction(transaction: RevenueTransaction) = dao.insertTransaction(transaction)

    suspend fun getPendingTransactions(): List<RevenueTransaction> = dao.getPendingTransactions()

    suspend fun markAsSynced(ids: List<String>) = dao.markAsSynced(ids)

    fun getTransactionByReceiptNo(receiptNo: String): Flow<RevenueTransaction?> = dao.getTransactionByReceiptNo(receiptNo)

    fun getCashUploadedTotal(): Flow<Double?> = dao.getCashUploadedTotal()
    
    fun getCashPendingTotal(): Flow<Double?> = dao.getCashPendingTotal()
    
    fun getPendingCount(): Flow<Int> = dao.getPendingCount()
}
