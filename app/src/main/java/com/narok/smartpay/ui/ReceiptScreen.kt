package com.narok.smartpay.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narok.smartpay.data.local.RevenueTransaction
import com.narok.smartpay.util.QRCodeGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    receiptNo: String,
    viewModel: RevenueViewModel,
    onNavigateHome: () -> Unit
) {
    val transaction by viewModel.getTransactionByReceiptNo(receiptNo).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE53935))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            transaction?.let { txn ->
                ReceiptContent(txn)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onNavigateHome,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Done / Next Customer", fontSize = 18.sp)
                }
            } ?: run {
                CircularProgressIndicator(modifier = Modifier.padding(top = 100.dp))
            }
        }
    }
}

@Composable
fun ReceiptContent(transaction: RevenueTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("COUNTY GOVERNMENT", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("OF NAROK", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Collection Receipt", fontSize = 16.sp, color = Color.Gray)
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            
            ReceiptRow("No", transaction.receiptNo)
            ReceiptRow("Stream", transaction.stream)
            ReceiptRow("Sub Group", transaction.subGroup)
            if (transaction.narration.isNotBlank()) {
                ReceiptRow("Narration", transaction.narration)
            }
            ReceiptRow("Served By", transaction.agentName)
            ReceiptRow("Section", transaction.section)
            
            val dateFormat = SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.getDefault())
            val dateString = dateFormat.format(Date(transaction.timestamp))
            ReceiptRow("Date", dateString)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${transaction.quantity} x Ksh ${transaction.unitPrice}")
                Text("Ksh ${transaction.unitPrice * transaction.quantity}")
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Ksh ${transaction.totalAmount}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // QR Code
            val qrBitmap: Bitmap? = remember(transaction.receiptNo) {
                QRCodeGenerator.generateQRCode(transaction.receiptNo, 300)
            }
            
            qrBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Receipt QR Code",
                    modifier = Modifier.size(150.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Scan to Verify", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.Medium, textAlign = TextAlign.End, modifier = Modifier.weight(2f))
    }
}
