package com.example.softnaroksmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.softnaroksmart.ui.theme.SOFTNAROKSMARTTheme
import com.narok.smartpay.data.Seeder
import com.narok.smartpay.ui.CollectionScreen
import com.narok.smartpay.ui.DashboardScreen
import com.narok.smartpay.ui.ReceiptScreen
import com.narok.smartpay.ui.RevenueViewModel
import com.narok.smartpay.ui.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Seed Database
        Seeder.seedInitialData(this)

        val factory = ViewModelFactory(this)
        val viewModel = ViewModelProvider(this, factory)[RevenueViewModel::class.java]

        setContent {
            SOFTNAROKSMARTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToCollection = {
                                    navController.navigate("collection")
                                },
                                onNavigateToReceipts = {
                                    // Handle reprint receipt
                                }
                            )
                        }
                        composable("collection") {
                            CollectionScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToReceipt = { receiptId ->
                                    navController.navigate("receipt/$receiptId")
                                }
                            )
                        }
                        composable("receipt/{receiptId}") { backStackEntry ->
                            val receiptId = backStackEntry.arguments?.getString("receiptId") ?: ""
                            ReceiptScreen(
                                receiptNo = receiptId,
                                viewModel = viewModel,
                                onNavigateHome = {
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
