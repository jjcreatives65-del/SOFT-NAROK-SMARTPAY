package com.narok.smartpay.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narok.smartpay.data.local.RevenueItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: RevenueViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReceipt: (String) -> Unit // pass receipt ID if generated
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Frequent", "Browse All")

    val frequentItems by viewModel.frequentItems.collectAsState()
    val allSubGroups by viewModel.allSubGroups.collectAsState()
    
    // State for selected items to show payment dialog
    var selectedItemForPayment by remember { mutableStateOf<RevenueItem?>(null) }
    
    // State to handle sub-group selection
    var selectedSubGroup by remember { mutableStateOf<String?>(null) }
    val subGroupItems by viewModel.currentSubGroupItems.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // App Bar
        TopAppBar(
            title = { Text("Select Revenue Type", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE53935))
        )

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { 
                        selectedTab = index 
                        selectedSubGroup = null // reset sub group when switching tabs
                    },
                    text = { Text(title) }
                )
            }
        }

        if (selectedTab == 0) {
            // Frequent Items
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(frequentItems) { item ->
                    RevenueItemRow(item) {
                        selectedItemForPayment = item
                    }
                }
            }
        } else {
            // Browse All
            if (selectedSubGroup == null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(allSubGroups) { subGroup ->
                        ListItem(
                            headlineContent = { Text(subGroup, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.clickable {
                                selectedSubGroup = subGroup
                                viewModel.loadItemsForSubGroup(subGroup)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            } else {
                // Showing items for subGroup
                Button(onClick = { selectedSubGroup = null }, modifier = Modifier.padding(8.dp)) {
                    Text("Back to Categories")
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(subGroupItems) { item ->
                        RevenueItemRow(item) {
                            selectedItemForPayment = item
                        }
                    }
                }
            }
        }
    }

    // Payment Dialog
    selectedItemForPayment?.let { item ->
        PaymentDialog(
            item = item,
            onDismiss = { selectedItemForPayment = null },
            onSubmit = { quantity, narration, isCash ->
                val receiptId = UUID.randomUUID().toString().take(15).uppercase()
                viewModel.submitTransaction(
                    item = item,
                    quantity = quantity,
                    narration = narration,
                    paymentMode = if (isCash) "CASH" else "MOBILE_MONEY",
                    receiptId = receiptId
                )
                selectedItemForPayment = null
                onNavigateToReceipt(receiptId)
            }
        )
    }
}

@Composable
fun RevenueItemRow(item: RevenueItem, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.stream, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(item.subGroup) },
        trailingContent = { Text("Ksh ${item.unitPrice}", color = Color.Red, fontWeight = FontWeight.Bold) },
        modifier = Modifier.clickable { onClick() }
    )
    HorizontalDivider()
}

@Composable
fun PaymentDialog(
    item: RevenueItem,
    onDismiss: () -> Unit,
    onSubmit: (Int, String, Boolean) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var narration by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(item.stream) },
        text = {
            Column {
                Text("Price: Ksh ${item.unitPrice}")
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quantity Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Quantity:")
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { if (quantity > 1) quantity-- }) { Text("-") }
                    Text("$quantity", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp)
                    Button(onClick = { quantity++ }) { Text("+") }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total: Ksh ${item.unitPrice * quantity}", fontWeight = FontWeight.Bold, color = Color.Red)
                
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = narration,
                    onValueChange = { narration = it },
                    label = { Text("Narration (e.g., KDE 150L)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(quantity, narration, true) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text("Pay Cash")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onSubmit(quantity, narration, false) }) {
                Text("Mobile Money")
            }
        }
    )
}
