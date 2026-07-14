package com.android.cyberqrscanner.navigation

import android.graphics.Camera
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.cyberqrscanner.ui.screens.CameraScannerUI
import com.android.cyberqrscanner.ui.screens.GeneratorScreenUI
import com.android.cyberqrscanner.ui.screens.ScannerScreenUI
import com.android.cyberqrscanner.ui.screens.SettingsScreenUI

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.ScannerScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
//        modifier = Modifier.padding(16.dp)
    ){
        composable<NavRoutes.ScannerScreen> {
            ScannerScreenUI(navController = navController)
        }

        composable<NavRoutes.GeneratorScreen> {
            GeneratorScreenUI(navController = navController)
        }

        composable<NavRoutes.SettingsScreen> {
            SettingsScreenUI(navController = navController)
        }

        composable<NavRoutes.CameraScanner> {
            CameraScannerUI(navController = navController)
        }
    }
}