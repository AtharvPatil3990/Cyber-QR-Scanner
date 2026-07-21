package com.android.cyberqrscanner.data

import com.google.mlkit.vision.barcode.common.Barcode

enum class QrType(value: String) {
    URL("URL"),
    WIFI("WiFi"),
    CONTACT("Contact"),
    EMAIL("Email"),
    PHONE("Phone"),
    SMS("SMS"),
    LOCATION("Location"),
    EVENT("Event"),
    ID_CARD("ID Card"),
    PRODUCT("Product"),
    TEXT("Text"),
    BOOK("Book"),
    RAW_DATA("Raw Data");

    companion object{
        fun toEnum(type: Int): QrType{
             return when(type) {
                 Barcode.TYPE_URL -> URL
                 Barcode.TYPE_WIFI -> WIFI
                 Barcode.TYPE_CONTACT_INFO -> CONTACT
                 Barcode.TYPE_EMAIL -> EMAIL
                 Barcode.TYPE_PHONE -> PHONE
                 Barcode.TYPE_SMS -> SMS
                 Barcode.TYPE_GEO -> LOCATION
                 Barcode.TYPE_CALENDAR_EVENT -> EVENT
                 Barcode.TYPE_DRIVER_LICENSE -> ID_CARD
                 Barcode.TYPE_PRODUCT -> PRODUCT
                 Barcode.TYPE_ISBN -> BOOK
                 Barcode.TYPE_TEXT -> TEXT
                 else -> RAW_DATA
             }
        }
    }
}