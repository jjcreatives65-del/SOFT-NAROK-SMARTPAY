package com.narok.smartpay.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narok.smartpay.data.local.RevenueItem
import com.narok.smartpay.data.local.RevenueTransaction
import com.narok.smartpay.data.repository.RevenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class RevenueViewModel(private val repository: RevenueRepository) : ViewModel() {

    // Current agent data (mocked for now, in a real app this comes from Auth)
    val currentAgentName = "WILSON MASIKONTE"
    val currentSection = "Narok Central"

    val frequentItems: StateFlow<List<RevenueItem>> = repository.getFrequentItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSubGroups: StateFlow<List<String>> = repository.getAllSubGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSubGroupItems = MutableStateFlow<List<RevenueItem>>(emptyList())
    val currentSubGroupItems: StateFlow<List<RevenueItem>> = _currentSubGroupItems.asStateFlow()
    
    val pendingSyncCount: StateFlow<Int> = repository.getPendingCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cashPendingTotal: StateFlow<Double?> = repository.getCashPendingTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cashUploadedTotal: StateFlow<Double?> = repository.getCashUploadedTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun loadItemsForSubGroup(subGroup: String) {
        viewModelScope.launch {
            repository.getItemsBySubGroup(subGroup).collect { items ->
                _currentSubGroupItems.value = items
            }
        }
    }

    fun getTransactionByReceiptNo(receiptNo: String): Flow<RevenueTransaction?> {
        return repository.getTransactionByReceiptNo(receiptNo)
    }

    fun submitTransaction(
        item: RevenueItem,
        quantity: Int,
        narration: String,
        paymentMode: String,
        receiptId: String
    ) {
        viewModelScope.launch {
            val total = item.unitPrice * quantity
            val transaction = RevenueTransaction(
                receiptNo = receiptId,
                stream = item.stream,
                subGroup = item.subGroup,
                narration = narration,
                unitPrice = item.unitPrice,
                quantity = quantity,
                totalAmount = total,
                paymentMode = paymentMode,
                agentName = currentAgentName,
                section = currentSection,
                timestamp = System.currentTimeMillis(),
                syncStatus = "PENDING"
            )
            repository.insertTransaction(transaction)
        }
    }
}
