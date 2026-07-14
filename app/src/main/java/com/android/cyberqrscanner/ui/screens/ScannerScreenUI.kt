package com.android.cyberqrscanner.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.cyberqrscanner.navigation.BottomNavBar
import com.android.cyberqrscanner.navigation.NavRoutes
import com.android.cyberqrscanner.ui.viewmodel.ScannerScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreenUI(navController: NavController) {

    val context = LocalContext.current
    val viewModel = viewModel<ScannerScreenViewModel>()
    val activity = context as? Activity

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted)
                navController.navigate(NavRoutes.CameraScanner)
            else {
                val shouldShowPermissionRational =
                    activity?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.CAMERA
                        )
                    } ?: false

                if (!shouldShowPermissionRational)
                    viewModel.onSettingsDialogShown()
            }
        }
    )

    if (viewModel.showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onSettingsDialogDismissed() },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onSettingsDialogDismissed()
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).also { context.startActivity(it) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text("Settings", fontWeight = FontWeight.Medium) }
            },
            dismissButton = {
                FilledTonalButton(
                    onClick = { viewModel.onSettingsDialogDismissed() }
                ) { Text("Cancel") }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera Icon"
                )
            },
            title = { Text("Camera Permission is required!") },
            text = { Text("To scan QR code camera permission is required, click 'Settings' to grant the permission.") },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            iconContentColor = MaterialTheme.colorScheme.secondary
        )
    }

//    Main screen ->
    Scaffold(
        bottomBar = { BottomNavBar(navController, "Scanner") },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "ScannerIcon"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Scanner", fontWeight = FontWeight.Medium)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
//                    Check if camera permission is granted
                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) ==
                        PackageManager.PERMISSION_GRANTED
                    )
                        navController.navigate(NavRoutes.CameraScanner)
                    else
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)

                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                text = { Text("Scan QR") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "QR Code Scanner"
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Recent Scan's",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}