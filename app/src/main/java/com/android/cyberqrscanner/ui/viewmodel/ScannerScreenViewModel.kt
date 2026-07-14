package com.android.cyberqrscanner.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel

class ScannerScreenViewModel : ViewModel(){
    var showSettingsDialog by mutableStateOf(false)
        private set

    fun onSettingsDialogShown(){
        showSettingsDialog = true
    }

    fun onSettingsDialogDismissed(){
        showSettingsDialog = false
    }

}