package com.android.cyberqrscanner.navigation

import kotlinx.serialization.Serializable

sealed class NavRoutes {

    @Serializable
    data object ScannerScreen: NavRoutes()

    @Serializable
    data object GeneratorScreen: NavRoutes()

    @Serializable
    data object SettingsScreen: NavRoutes()

    @Serializable
    data object CameraScanner: NavRoutes()

    @Serializable
    data class ScannedResultScreen(val scanId: Long): NavRoutes()
}