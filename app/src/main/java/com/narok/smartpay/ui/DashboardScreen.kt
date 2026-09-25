package com.narok.smartpay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    viewModel: RevenueViewModel,
    onNavigateToCollection: () -> Unit,
    onNavigateToReceipts: () -> Unit
) {
    val cashUploaded = viewModel.cashUploadedTotal.collectAsState().value ?: 0.0
    val cashPending = viewModel.cashPendingTotal.collectAsState().value ?: 0.0
    val pendingCount = viewModel.pendingSyncCount.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Z-Report", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(viewModel.currentAgentName, color = Color.White, fontSize = 18.sp)
                Text("Section: ${viewModel.currentSection}", color = Color.White, fontSize = 16.sp)
            }
        }

        // Stats
        Text("Collections", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatBox(title = "Cash Uploaded", amount = cashUploaded)
            StatBox(title = "Cash Pending", amount = cashPending, count = pendingCount)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Button(
            onClick = onNavigateToCollection,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
        ) {
            Text("Collections (New Receipt)", fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onNavigateToReceipts,
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Reprint Receipt", fontSize = 18.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Handle Cache Sync */ },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Cache Data ($pendingCount Pending)", fontSize = 18.sp, color = Color.Black)
        }
    }
}

@Composable
fun StatBox(title: String, amount: Double, count: Int? = null) {
    Card(
        modifier = Modifier.width(160.dp).height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 14.sp, color = Color.Gray)
            Text("Ksh $amount", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (count != null) {
                Text("$count items", fontSize = 12.sp, color = Color.Red)
            }
        }
    }
}
