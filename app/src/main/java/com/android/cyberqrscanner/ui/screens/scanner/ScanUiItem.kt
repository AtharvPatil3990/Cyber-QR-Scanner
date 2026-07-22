package com.android.cyberqrscanner.ui.screens.scanner

import com.android.cyberqrscanner.data.QrType

data class ScanUiItem(
    val id: Long,
    val timestamp: String,
    val rawValue: String,
    val qrType: QrType
)
