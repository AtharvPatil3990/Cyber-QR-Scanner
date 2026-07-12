package com.android.cyberqrscanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generate_qr_history")
data class GenerateQrHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val qrType: String
)