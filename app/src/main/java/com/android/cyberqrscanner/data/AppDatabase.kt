package com.android.cyberqrscanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.cyberqrscanner.data.converter.Converter
import com.android.cyberqrscanner.data.dao.GenerateHistoryDao
import com.android.cyberqrscanner.data.dao.ScanHistoryDao
import com.android.cyberqrscanner.data.entities.GenerateQrEntity
import com.android.cyberqrscanner.data.entities.ScanQrEntity

@Database(entities = [GenerateQrEntity::class, ScanQrEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun generateHistoryDao(): GenerateHistoryDao

}