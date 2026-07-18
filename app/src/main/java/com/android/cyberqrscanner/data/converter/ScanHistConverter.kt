package com.android.cyberqrscanner.data.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converter {
    @TypeConverter
    fun fromMapToString(map: Map<String, String>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun fromStringToMap(value: String): Map<String, String> {
        return try {
            Json.decodeFromString(value)
        }
        catch (e : Exception){
            emptyMap<String, String>()
        }
    }
}