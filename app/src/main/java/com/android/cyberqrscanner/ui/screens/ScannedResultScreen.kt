package com.android.cyberqrscanner.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
fun ScannedResultScreen(navController: NavController, scanId: Long) {

    Box(
        contentAlignment = Alignment.Center
    ){
        Text("Scanned QR id: $scanId")
    }

}