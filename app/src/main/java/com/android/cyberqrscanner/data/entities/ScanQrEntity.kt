package com.android.cyberqrscanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.cyberqrscanner.data.QrType

@Entity(tableName = "scan_qr_history")
data class ScanQrEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val rawValue: String,

    val qrType: QrType,

    val parsedData: Map<String, String>,

//    Save time (in long)
    val timestamp: Long,

//  Allow user to mark favourites / bookmark
    val isFavourite: Boolean,

    val customType: String
)