package com.android.cyberqrscanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.android.cyberqrscanner.data.dao.GenerateHistoryDao
import com.android.cyberqrscanner.data.dao.ScanHistoryDao
import com.android.cyberqrscanner.data.entities.GenerateQrHistory
import com.android.cyberqrscanner.data.entities.ScanQrHistory

@Database(entities = [GenerateQrHistory::class, ScanQrHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun generateHistoryDao(): GenerateHistoryDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cyber_qr_scanner"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

}