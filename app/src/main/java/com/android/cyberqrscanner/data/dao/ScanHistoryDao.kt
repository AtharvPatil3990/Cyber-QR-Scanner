package com.android.cyberqrscanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.android.cyberqrscanner.data.entities.ScanQrEntity

@Dao
interface ScanHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanQr(qr: ScanQrEntity): Long
}