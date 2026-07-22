package com.android.cyberqrscanner.ui.screens.scanner

import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.cyberqrscanner.MyApplication
import com.android.cyberqrscanner.navigation.BottomNavBar
import com.android.cyberqrscanner.navigation.NavRoutes
import com.android.cyberqrscanner.ui.factories.ScannerViewModelFactory
import com.android.cyberqrscanner.ui.viewmodel.ScannerScreenViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun ScannerScreenUI(navController: NavController) {

    val context = LocalContext.current
    val appContext = context.applicationContext as MyApplication
    val viewModel: ScannerScreenViewModel =
        viewModel(factory = ScannerViewModelFactory(appContext.scanRepo))

    val scanner = remember {
        GmsBarcodeScanning
            .getClient(
                context,
                GmsBarcodeScannerOptions.Builder().enableAutoZoom().build()
            )
    }

    var showClearHistoryDialog by remember { mutableStateOf(false) }

//    ScanHistory List
    val scansList by viewModel.scanHistoryState.collectAsStateWithLifecycle()

//    For bottom sheet
    var showBottomsheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

//    Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    viewModel.processImage(
                        InputImage.fromFilePath(context, uri)
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to load image file", Toast.LENGTH_LONG).show()
                    Log.e("Error", e.message ?: "Error while fetching image")
                }
            }
        }
    )

//    LaunchedEffect for opening the result of QR
    LaunchedEffect(viewModel.navigateToResultId) {
        viewModel.navigateToResultId?.let { scanId ->
            navController.navigate(NavRoutes.ScannedResultScreen(scanId))
            viewModel.onResultNavigationHandled()
        }
    }

//    Listen for errors from viewModel
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    if(showClearHistoryDialog){
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearHistoryDialog = false

                        viewModel.clearAllHistory()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Delete", fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearHistoryDialog = false },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) { Text("Cancel") }
            },
            title = { Text("Clear All Scan History?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete all scanned items. This action cannot be undone.") },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            iconContentColor = MaterialTheme.colorScheme.secondary
        )
    }

    if (showBottomsheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomsheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        showBottomsheet = false
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                if (barcode != null)
                                    viewModel.insertBarcodeInDB(barcode)
                                else
                                    viewModel.showError("Couldn't detect QR, try again!")
                            }
                            .addOnFailureListener {
                                viewModel.showError("An Error occurred, please try again!")
                            }
                            .addOnCanceledListener {
                                Toast.makeText(context, "Scan Cancelled.", Toast.LENGTH_LONG).show()
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Icon"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Scan QR with Camera", fontSize = 16.sp)
                }

                FilledTonalButton(
                    onClick = {
                        showBottomsheet = false
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.filledTonalButtonColors()
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Image Icon"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Upload image from Gallery", fontSize = 14.sp)
                }
            }
        }
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
                            contentDescription = "ScannerIcon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Scanner", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showBottomsheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,    // Inner text color,
                text = { Text("Scan") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "QR Code Scanner"
                    )
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 10.dp, start = 12.dp, end = 12.dp)
                .fillMaxSize()
        ) {

//            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Scan's",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                TextButton(
                    onClick = {
                        showClearHistoryDialog = true
                    },
                    enabled = scansList.isNotEmpty()
                ) {
                    Text(text = "Clear All", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if(scansList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(items = scansList) { scan ->
                        ScanHistoryItem(
                            onItemClick = {},
                            onDeleteClick = {
                                viewModel.deleteScannedQr(scan.id)
                            },
                            timestamp = scan.timestamp,
                            qrType = scan.qrType,
                            rawValue = scan.rawValue
                        )
                    }
                }
            }
            else{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Scan History!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}