package com.android.cyberqrscanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController, currItem: String) {

    val navItemsList = listOf(
        NavItem("Scanner", Icons.Default.CameraAlt, NavRoutes.ScannerScreen),
        NavItem("Generator", Icons.Outlined.AutoAwesomeMotion, NavRoutes.GeneratorScreen),
        NavItem("Settings", Icons.Default.Settings, NavRoutes.SettingsScreen)
    )

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        tonalElevation = 0.dp
    ) {
        navItemsList.forEach { item ->

            NavigationBarItem(
                selected = item.label == currItem,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if(currItem == item.label) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    // ACTIVE STATE: The brand dictates "Primary" is used for active states.
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onTertiary,

                    // UNSELECTED STATE: Must contrast against the dark Tertiary background.
                    // We use onTertiary (White) for contrast, and Compose automatically
                    // applies a standard 60% alpha to unselected items to dim them.
                    unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.onTertiary
                )
            )
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: NavRoutes
)