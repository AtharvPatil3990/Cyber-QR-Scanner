package com.android.cyberqrscanner.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.cyberqrscanner.repository.ScanRepository
import com.android.cyberqrscanner.ui.viewmodel.ScannerScreenViewModel

@Suppress("UNCHECKED_CAST")
class ScannerViewModelFactory(
    private val scanRepository: ScanRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerScreenViewModel::class.java))
            return ScannerScreenViewModel(scanRepository = scanRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}