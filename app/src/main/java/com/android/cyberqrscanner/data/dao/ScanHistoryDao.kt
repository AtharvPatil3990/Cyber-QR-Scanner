package com.android.cyberqrscanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("DELETE FROM scan_qr_history WHERE id = :id")
    suspend fun deleteScanQr(id: Long)

    @Query("DELETE FROM scan_qr_history")
    suspend fun clearAllHistory()
}