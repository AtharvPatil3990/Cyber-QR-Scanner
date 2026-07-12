package com.android.cyberqrscanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_qr_history")
data class ScanQrHistory (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val rawValue: String,

//    Wifi,
    val qrType: String,

//    Save time (in long)
    val timestamp: Long,

//  Allow user to mark favourites / bookmark
    val isFavourite: Boolean,

    val customType: String
)