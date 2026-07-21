package com.android.cyberqrscanner.ui.screens.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.cyberqrscanner.data.QrType


val QrType.icon: ImageVector
    get() = when (this) {
        QrType.URL -> Icons.Default.Link
        QrType.LOCATION -> Icons.Default.LocationOn
        QrType.EMAIL -> Icons.Default.Email
        QrType.SMS -> Icons.Default.Sms
        QrType.BOOK -> Icons.Default.Book
        QrType.CONTACT -> Icons.Default.Person
        QrType.EVENT -> Icons.Default.Event
        QrType.ID_CARD -> Icons.Default.Badge
        QrType.PHONE -> Icons.Default.Phone
        QrType.PRODUCT -> Icons.Default.ShoppingCart
        QrType.TEXT -> Icons.Default.Description
        QrType.WIFI -> Icons.Default.Wifi
        QrType.RAW_DATA -> Icons.Default.QrCode2
    }

@Composable
fun ScanHistoryItem(
    onItemClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    timestamp: String,
    qrType: QrType,
    rawValue: String
) {
    ListItem(
        modifier = Modifier.clickable(
            onClick = onItemClick
        ),
        colors = ListItemDefaults.colors(
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ),
        overlineContent = {
            Text(
                text = qrType.name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        leadingContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = qrType.icon,
                    contentDescription = "Icon of qr type",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        headlineContent = {
            Text(
                text = rawValue,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1, // Crucial: prevents layout breaking on huge QR payloads
                overflow = TextOverflow.Ellipsis // Adds the "..." at the end
            )
        },
        supportingContent = {
            Text(
                text = timestamp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCopyClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}