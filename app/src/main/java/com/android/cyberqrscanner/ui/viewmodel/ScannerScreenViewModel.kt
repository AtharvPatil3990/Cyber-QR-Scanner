package com.android.cyberqrscanner.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cyberqrscanner.data.entities.ScanQrEntity
import com.android.cyberqrscanner.repository.ScanRepository
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScannerScreenViewModel(
    private val scanRepository: ScanRepository
) : ViewModel(){

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var navigateToResultId by mutableStateOf<Long?>(null)
        private set

    val scanHistoryState = scanRepository.getScanHist().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onResultNavigationHandled(){
        navigateToResultId = null
    }

    fun processImage(inputImage: InputImage){
        val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().build())

        scanner
            .process(inputImage)
            .addOnSuccessListener { barcodes ->
                if(barcodes.isNotEmpty()){
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

    fun insertBarcodeInDB(barcode: Barcode){
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

            withContext(Dispatchers.Main){
                navigateToResultId = newScanId
            }
        }
    }

    fun showError(message: String) {
        viewModelScope.launch {
            _uiEvent.send(message)
        }
    }

}