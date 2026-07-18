package com.android.cyberqrscanner

import android.app.Application
import androidx.room.Room
import com.android.cyberqrscanner.data.AppDatabase
import com.android.cyberqrscanner.repository.ScanRepository

class MyApplication: Application() {

    val database by lazy{
        Room.databaseBuilder(
            this@MyApplication,
            AppDatabase::class.java,
            "cyber_qr_scanner"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    val scanRepo by lazy{
        ScanRepository(database.scanHistoryDao())
    }

}