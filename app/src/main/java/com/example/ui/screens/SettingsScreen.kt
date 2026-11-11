package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppThemeMode
import com.example.ui.components.ConversationConfigDialog
import com.example.ui.components.TogetherAvatar
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.WhatsAppLinkingCard
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherWarmPink
import com.example.ui.viewmodel.TogetherViewModel

@Composable
fun SettingsScreen(
    viewModel: TogetherViewModel,
    modifier: Modifier = Modifier
) {
    val activePartnerId by viewModel.activePartnerId.collectAsState()
    val isSharingSms by viewModel.isSharingSms.collectAsState()
    val isSharingCalls by viewModel.isSharingCalls.collectAsState()
    val isSharingWhatsApp by viewModel.isSharingWhatsApp.collectAsState()
    val isSharingPaused by viewModel.isSharingPaused.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val isWhatsAppLinked by viewModel.isWhatsAppLinked.collectAsState()
    val linkedDeviceName by viewModel.linkedDeviceName.collectAsState()
    val whatsAppChats by viewModel.whatsAppChats.collectAsState()
    val smsContacts by viewModel.smsContacts.collectAsState()

    var showRevokeDialog by remember { mutableStateOf(false) }
    var showQrDialog by remember { mutableStateOf(false) }
    var showConfigDialog by remember { mutableStateOf(false) }
    var biometricLockEnabled by remember { mutableStateOf(false) }
    var cacheClearedToast by remember { mutableStateOf(false) }

    val currentName = if (activePartnerId == "ALEX") "Alex" else "Sam"
    val partnerName = if (activePartnerId == "ALEX") "Sam" else "Alex"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Preferences, privacy controls, and companion devices",
            style = MaterialTheme.typography.bodyMedium,
            color = TogetherSecondaryText
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Profile Card (Without the Switch Partner button)
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TogetherAvatar(
                    name = currentName,
                    size = 48.dp,
                    isFirstPartner = activePartnerId == "ALEX"
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$currentName",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Paired with $partnerName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TogetherPrimaryGreen
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEAF7E2))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Active Pair",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TogetherPrimaryGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 1. APPEARANCE & THEME SECTION
        SettingsSectionHeader(title = "Appearance & Theme")

        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Theme Preference",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Choose light, dark, or follow device system settings",
                    fontSize = 12.sp,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionTile(
                        label = "Light",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == AppThemeMode.LIGHT,
                        onClick = { viewModel.setThemeMode(AppThemeMode.LIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionTile(
                        label = "Dark",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == AppThemeMode.DARK,
                        onClick = { viewModel.setThemeMode(AppThemeMode.DARK) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionTile(
                        label = "System",
                        icon = Icons.Default.BrightnessAuto,
                        isSelected = themeMode == AppThemeMode.SYSTEM,
                        onClick = { viewModel.setThemeMode(AppThemeMode.SYSTEM) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 2. WHATSAPP & COMPANION DEVICES SECTION
        SettingsSectionHeader(title = "Companion Connections")

        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isWhatsAppLinked) Color(0xFFEAF7E2) else Color(0xFFF2F2F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Devices,
                            contentDescription = null,
                            tint = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "WhatsApp Web Companion",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isWhatsAppLinked) (linkedDeviceName ?: "WhatsApp Web") else "Not connected to WhatsApp Web",
                            fontSize = 12.sp,
                            color = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isWhatsAppLinked) Color(0xFFEAF7E2) else Color(0xFFEEEEEC))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isWhatsAppLinked) "Connected" else "Idle",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isWhatsAppLinked) {
                        TogetherButton(
                            text = "Link via QR Code",
                            onClick = { showQrDialog = true },
                            variant = TogetherButtonVariant.PRIMARY_GREEN,
                            icon = Icons.Default.QrCodeScanner,
                            height = 36.dp,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        TogetherButton(
                            text = "Unlink Device",
                            onClick = { viewModel.unlinkWhatsApp() },
                            variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                            height = 36.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TogetherButton(
                        text = "Selective Sharing",
                        onClick = { showConfigDialog = true },
                        variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                        icon = Icons.Outlined.Tune,
                        height = 36.dp,
                        modifier = Modifier.weight(1.1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 3. PRIVACY & SHARING PERMISSIONS
        SettingsSectionHeader(title = "Privacy & Consent")

        // Master Pause Card
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pause All Sharing",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isSharingPaused) "All feeds currently paused for your partner." else "Temporarily pause feeds without clearing history.",
                        fontSize = 12.sp,
                        color = TogetherSecondaryText
                    )
                }

                Switch(
                    checked = isSharingPaused,
                    onCheckedChange = { viewModel.togglePauseSharing() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = TogetherPrimaryGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selective category switches
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                SettingToggleRow(
                    title = "SMS Conversations",
                    subtitle = "Share text messages from selected phone numbers",
                    icon = Icons.Outlined.MailOutline,
                    isChecked = isSharingSms,
                    onCheckedChange = { viewModel.toggleShareSms() }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TogetherBorder)
                )

                SettingToggleRow(
                    title = "Call History",
                    subtitle = "Share incoming and outgoing call records",
                    icon = Icons.Outlined.Call,
                    isChecked = isSharingCalls,
                    onCheckedChange = { viewModel.toggleShareCalls() }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TogetherBorder)
                )

                SettingToggleRow(
                    title = "WhatsApp Shared Chats",
                    subtitle = "Allow linked WhatsApp messages in shared feed",
                    icon = Icons.Outlined.ChatBubbleOutline,
                    isChecked = isSharingWhatsApp,
                    onCheckedChange = { viewModel.toggleShareWhatsApp() }
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 4. SECURITY SECTION
        SettingsSectionHeader(title = "Security & Encryption")

        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAF7E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = TogetherPrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "End-to-End Encrypted",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Direct peer-to-peer verification active",
                            fontSize = 12.sp,
                            color = TogetherSecondaryText
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = TogetherPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TogetherBorder)
                )

                SettingToggleRow(
                    title = "Biometric Lock",
                    subtitle = "Require fingerprint or PIN upon app launch",
                    icon = Icons.Default.Fingerprint,
                    isChecked = biometricLockEnabled,
                    onCheckedChange = { biometricLockEnabled = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 5. DATA & STORAGE MANAGEMENT
        SettingsSectionHeader(title = "Storage & Local Cache")

        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Local Cached Media",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (cacheClearedToast) "Cache cleared (0 KB)" else "14.2 MB cached logs and thumbnails",
                            fontSize = 12.sp,
                            color = TogetherSecondaryText
                        )
                    }

                    TogetherButton(
                        text = "Clear Cache",
                        onClick = { cacheClearedToast = true },
                        variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                        icon = Icons.Outlined.CleaningServices,
                        height = 34.dp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TogetherBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Revoke and Delete Shared Data",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TogetherWarmPink
                        )
                        Text(
                            text = "Instantly clears all your shared logs from partner's view",
                            fontSize = 12.sp,
                            color = TogetherSecondaryText
                        )
                    }

                    TogetherButton(
                        text = "Revoke",
                        onClick = { showRevokeDialog = true },
                        variant = TogetherButtonVariant.WARM_PINK,
                        icon = Icons.Default.DeleteOutline,
                        height = 34.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Replay onboarding & About
        TogetherButton(
            text = "Replay Introduction Walkthrough",
            onClick = { viewModel.restartOnboarding() },
            variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
            icon = Icons.Default.Refresh,
            height = 40.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = TogetherSecondaryText,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "TogetherSync v2.4 (Build 240) • Private Couple Sync",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TogetherSecondaryText
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // QR Scanner Dialog for WhatsApp Web linking
    if (showQrDialog) {
        Dialog(onDismissRequest = { showQrDialog = false }) {
            WhatsAppLinkingCard(
                isLinked = isWhatsAppLinked,
                linkedDeviceName = linkedDeviceName,
                onSimulateScanLink = {
                    viewModel.linkWhatsApp()
                    showQrDialog = false
                },
                onUnlink = {
                    viewModel.unlinkWhatsApp()
                    showQrDialog = false
                },
                onManageSharing = {
                    showQrDialog = false
                    showConfigDialog = true
                }
            )
        }
    }

    // Selective Sharing Configuration Dialog
    if (showConfigDialog) {
        ConversationConfigDialog(
            whatsAppChats = whatsAppChats,
            smsContacts = smsContacts,
            onToggleWhatsAppChat = { viewModel.toggleWhatsAppChatSharing(it) },
            onToggleSmsContact = { viewModel.toggleSmsContactSharing(it) },
            onUpdateSmsContact = { oldNum, name, newNum ->
                viewModel.updateSmsContact(oldNum, name, newNum)
            },
            onDismiss = { showConfigDialog = false }
        )
    }

    if (showRevokeDialog) {
        AlertDialog(
            onDismissRequest = { showRevokeDialog = false },
            title = {
                Text(
                    text = "Revoke All Shared Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Text(
                    text = "This will immediately clear all messages and call records from your shared partner space. Are you sure you wish to proceed?",
                    fontSize = 13.sp,
                    color = TogetherSecondaryText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.revokeMyData()
                        showRevokeDialog = false
                    }
                ) {
                    Text(
                        text = "Delete Data",
                        color = TogetherWarmPink,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevokeDialog = false }) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Medium,
                        color = TogetherSecondaryText
                    )
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ThemeOptionTile(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) TogetherPrimaryGreen else MaterialTheme.colorScheme.surface)
            .border(
                1.5.dp,
                if (isSelected) TogetherPrimaryGreen else TogetherBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else TogetherSecondaryText,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TogetherPrimaryText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TogetherSecondaryText
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TogetherPrimaryGreen
            )
        )
    }
}
