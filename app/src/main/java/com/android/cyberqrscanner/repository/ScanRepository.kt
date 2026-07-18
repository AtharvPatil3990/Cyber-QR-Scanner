package com.android.cyberqrscanner.repository

import com.android.cyberqrscanner.data.dao.ScanHistoryDao
import com.android.cyberqrscanner.data.entities.ScanQrEntity
import com.google.mlkit.vision.barcode.common.Barcode

class ScanRepository(
    private val scanDao: ScanHistoryDao
) {

    suspend fun insertScannedQr(qr: ScanQrEntity): Long{
        return scanDao.insertScanQr(qr = qr)
    }

    fun extractQrType(valueType: Int): String {
        return when(valueType){
            Barcode.TYPE_URL -> "URL"
            Barcode.TYPE_WIFI -> "WiFi"
            Barcode.TYPE_CONTACT_INFO -> "Contact"
            Barcode.TYPE_EMAIL -> "Email"
            Barcode.TYPE_PHONE -> "Phone"
            Barcode.TYPE_SMS -> "SMS"
            Barcode.TYPE_GEO -> "Location"
            Barcode.TYPE_CALENDAR_EVENT -> "Event"
            Barcode.TYPE_DRIVER_LICENSE -> "ID Card"  // <-- ADDED FOR PDF417
            Barcode.TYPE_PRODUCT -> "Product"         // <-- ADDED FOR RETAIL BARCODES
            else -> "Text"
        }
    }

    fun extractBarcodeData(barcode: Barcode): Map<String, String> {
        val dataMap = mutableMapOf<String, String>()

        when (barcode.valueType) {
            Barcode.TYPE_WIFI -> {
                barcode.wifi?.let { wifi ->
                    wifi.ssid?.let { dataMap["Network Name"] = it }
                    wifi.password?.let { dataMap["Password"] = it }
                    // Optional: Map integer to string for security type
                    val security = when (wifi.encryptionType) {
                        Barcode.WiFi.TYPE_WPA -> "WPA/WPA2"
                        Barcode.WiFi.TYPE_WEP -> "WEP"
                        Barcode.WiFi.TYPE_OPEN -> "Open"
                        else -> "Unknown"
                    }
                    dataMap["Security"] = security
                }
            }

            Barcode.TYPE_CONTACT_INFO -> {
                barcode.contactInfo?.let { contact ->
                    contact.name?.formattedName?.let { dataMap["Name"] = it }
                    contact.phones.firstOrNull()?.number?.let { dataMap["Phone"] = it }
                    contact.emails.firstOrNull()?.address?.let { dataMap["Email"] = it }
                    contact.organization?.let { dataMap["Company"] = it }
                }
            }

            Barcode.TYPE_URL -> {
                barcode.url?.let { url ->
                    url.url?.let { dataMap["URL"] = it }
                    // Some QR URLs contain titles, though rare
                    url.title?.takeIf { it.isNotBlank() }?.let { dataMap["Title"] = it }
                }
            }

            Barcode.TYPE_EMAIL -> {
                barcode.email?.let { email ->
                    email.address?.let { dataMap["To"] = it }
                    email.subject?.takeIf { it.isNotBlank() }?.let { dataMap["Subject"] = it }
                    email.body?.takeIf { it.isNotBlank() }?.let { dataMap["Body"] = it }
                }
            }

            Barcode.TYPE_GEO -> {
                barcode.geoPoint?.let { geo ->
                    dataMap["Latitude"] = geo.lat.toString()
                    dataMap["Longitude"] = geo.lng.toString()
                }
            }

            // The fallback for Plain Text, simple UPC barcodes, or unmapped types
            else -> {
                dataMap["Text Data"] = barcode.rawValue ?: "No data"
            }
        }

        return dataMap
    }
}