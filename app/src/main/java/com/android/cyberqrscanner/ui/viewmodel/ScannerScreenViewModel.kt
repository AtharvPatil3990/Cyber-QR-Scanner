package com.android.cyberqrscanner.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cyberqrscanner.data.entities.ScanQrEntity
import com.android.cyberqrscanner.repository.ScanRepository
import com.android.cyberqrscanner.ui.screens.scanner.ScanUiItem
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale

class ScannerScreenViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var navigateToResultId by mutableStateOf<Long?>(null)
        private set

    val scanHistoryState = scanRepository.getScanHist()
        .map { dbEntities ->
            dbEntities.map { entity ->
                ScanUiItem(
                    id = entity.id,
                    timestamp = entity.timestamp.toFormattedDate(),
                    rawValue = entity.rawValue,
                    qrType = entity.qrType
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onResultNavigationHandled() {
        navigateToResultId = null
    }

    fun processImage(inputImage: InputImage) {
        val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().build())

        scanner
            .process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val barcode = barcodes.first() ?: return@addOnSuccessListener

                    insertBarcodeInDB(barcode)

                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    showError("Failed to recognize QR in image.")
                }
            }
    }

    fun insertBarcodeInDB(barcode: Barcode) {
        val rawValue = barcode.rawValue ?: return

        val qrType = scanRepository.extractQrType(barcode.valueType)
        val barcodeData = scanRepository.extractBarcodeData(barcode)

        val newScan = ScanQrEntity(
            rawValue = rawValue,
            qrType = qrType,
            timestamp = System.currentTimeMillis(),
            isFavourite = false,
            parsedData = barcodeData,
            customType = ""
        )

        viewModelScope.launch(Dispatchers.IO) {
            val newScanId = scanRepository.insertScannedQr(newScan)

            withContext(Dispatchers.Main) {
                navigateToResultId = newScanId
            }
        }
    }

    fun deleteScannedQr(id: Long){
        viewModelScope.launch {
            scanRepository.deleteScannedQr(id)
        }
    }

    fun clearAllHistory(){
        viewModelScope.launch {
            scanRepository.clearAllHistory()
        }
    }

    fun showError(message: String) {
        viewModelScope.launch {
            _uiEvent.send(message)
        }
    }

    fun Long.toFormattedDate(): String {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId)
        val scanDateTime = Instant.ofEpochMilli(this).atZone(zoneId)

        val scanDate = scanDateTime.toLocalDate()
        val timeFormatter = ofPattern("hh:mm a", Locale.getDefault())

        return when {
            scanDate == today -> {
                "Today, ${scanDateTime.format(timeFormatter)}"
            }

            scanDate == today.minusDays(1) -> {
                "Yesterday, ${scanDateTime.format(timeFormatter)}"
            }

            scanDate.year == today.year -> {
                scanDateTime.format(ofPattern("MMM dd, hh:mm a", Locale.getDefault()))
            }

            else -> {
                scanDateTime.format(ofPattern("MMM dd yyyy, hh:mm a", Locale.getDefault()))
            }
        }
    }

}