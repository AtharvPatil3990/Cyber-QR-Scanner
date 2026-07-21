package com.android.cyberqrscanner.data.converter

import androidx.room.TypeConverter
import com.android.cyberqrscanner.data.QrType

class QrTypeConverter {

    @TypeConverter
    fun fromQrType(type: QrType): String{
        return type.name
    }

    @TypeConverter
    fun toQrType(type: String): QrType{
        return try {
            QrType.valueOf(type)
        }
        catch (e: Exception){
            QrType.RAW_DATA
        }
    }

}