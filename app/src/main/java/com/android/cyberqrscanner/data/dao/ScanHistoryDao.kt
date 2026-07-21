package com.android.cyberqrscanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.cyberqrscanner.data.entities.ScanQrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanQr(qr: ScanQrEntity): Long

    @Query("SELECT * FROM scan_qr_history ORDER BY id DESC")
    fun getScanHistory(): Flow<List<ScanQrEntity>>

}