package com.android.cyberqrscanner.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.cyberqrscanner.navigation.BottomNavBar

@Composable
fun GeneratorScreenUI(navController: NavController) {

    Scaffold(
        bottomBar = { BottomNavBar(navController, "Generator") },
        topBar = { Text("Generator") }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Generator")
        }

    }
}