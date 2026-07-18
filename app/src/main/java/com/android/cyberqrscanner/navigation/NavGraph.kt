package com.android.cyberqrscanner.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.cyberqrscanner.ui.screens.scanner.CameraScannerUI
import com.android.cyberqrscanner.ui.screens.GeneratorScreenUI
import com.android.cyberqrscanner.ui.screens.ScannedResultScreen
import com.android.cyberqrscanner.ui.screens.scanner.ScannerScreenUI
import com.android.cyberqrscanner.ui.screens.SettingsScreenUI

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.ScannerScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
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

        composable<NavRoutes.ScannedResultScreen> { backStackEntry ->
            val routeData = backStackEntry.toRoute<NavRoutes.ScannedResultScreen>()

            ScannedResultScreen(navController = navController, scanId = routeData.scanId)
        }
    }
}