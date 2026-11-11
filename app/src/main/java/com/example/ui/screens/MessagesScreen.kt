package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MessageEntity
import com.example.data.model.SourceType
import com.example.ui.components.ConversationConfigDialog
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.TogetherEmptyState
import com.example.ui.components.TogetherMessageBubble
import com.example.ui.components.WhatsAppLinkingCard
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.viewmodel.TogetherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: TogetherViewModel,
    modifier: Modifier = Modifier
) {
    val activePartnerId by viewModel.activePartnerId.collectAsState()
    val allMessages by viewModel.messages.collectAsState()
    val searchQuery by viewModel.messageSearchQuery.collectAsState()
    val sourceFilter by viewModel.messageSourceFilter.collectAsState()
    val isSharingSms by viewModel.isSharingSms.collectAsState()
    val isSharingWhatsApp by viewModel.isSharingWhatsApp.collectAsState()
    val isSharingPaused by viewModel.isSharingPaused.collectAsState()

    // WhatsApp Linking & Selective Sharing state
    val isWhatsAppLinked by viewModel.isWhatsAppLinked.collectAsState()
    val linkedDeviceName by viewModel.linkedDeviceName.collectAsState()
    val whatsAppChats by viewModel.whatsAppChats.collectAsState()
    val smsContacts by viewModel.smsContacts.collectAsState()

    var showQrLinkDialog by remember { mutableStateOf(false) }
    var showConfigDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var newMessageText by remember { mutableStateOf("") }
    var selectedSendSource by remember { mutableStateOf(SourceType.SMS) }

    val filteredMessages = allMessages.filter { msg ->
        val matchesSource = when (sourceFilter) {
            "SMS" -> msg.sourceType == SourceType.SMS
            "WHATSAPP" -> msg.sourceType == SourceType.WHATSAPP
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                msg.text.contains(searchQuery, ignoreCase = true) ||
                msg.senderName.contains(searchQuery, ignoreCase = true)
        val matchesPrivacy = !isSharingPaused && when (msg.sourceType) {
            SourceType.SMS -> isSharingSms
            SourceType.WHATSAPP -> isSharingWhatsApp
        }
        matchesSource && matchesSearch && matchesPrivacy
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Viewing as ${if (activePartnerId == "ALEX") "Alex" else "Sam"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TogetherSecondaryText
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TogetherButton(
                        text = if (isWhatsAppLinked) "Sharing Options" else "Link WhatsApp",
                        onClick = {
                            if (isWhatsAppLinked) {
                                showConfigDialog = true
                            } else {
                                showQrLinkDialog = true
                            }
                        },
                        variant = if (isWhatsAppLinked) TogetherButtonVariant.OUTLINED_NEUTRAL else TogetherButtonVariant.PRIMARY_GREEN,
                        icon = if (isWhatsAppLinked) Icons.Default.Tune else Icons.Default.QrCodeScanner,
                        height = 38.dp,
                        testTag = "link_whatsapp_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // WhatsApp Web Status / Link Banner
            TogetherCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isWhatsAppLinked) Color(0xFFF7FAF6) else TogetherCardBg,
                borderColor = if (isWhatsAppLinked) Color(0xFFC8EAB5) else TogetherBorder,
                cornerRadius = 14.dp,
                onClick = {
                    if (isWhatsAppLinked) {
                        showConfigDialog = true
                    } else {
                        showQrLinkDialog = true
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isWhatsAppLinked) TogetherSoftGreen else Color(0xFFEDF2F7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isWhatsAppLinked) Icons.Default.CheckCircle else Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isWhatsAppLinked) "WhatsApp Web Linked • Live" else "Link WhatsApp with QR Code",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = TogetherPrimaryText
                            )
                            Text(
                                text = if (isWhatsAppLinked) "Tap to change shared chats & numbers" else "Scan QR code to see all chats live",
                                fontSize = 11.sp,
                                color = TogetherSecondaryText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isWhatsAppLinked) TogetherSoftGreen else Color(0xFFEEEEEE))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isWhatsAppLinked) "MANAGE" else "SCAN QR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWhatsAppLinked) Color(0xFF287D17) else TogetherSecondaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setMessageSearchQuery(it) },
                placeholder = {
                    Text(
                        "Search conversations...",
                        fontSize = 13.sp,
                        color = TogetherSecondaryText
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TogetherSecondaryText,
                        modifier = Modifier.size(18.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = TogetherPrimaryGreen
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("message_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips + Selective Sharing Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterPill(
                        label = "All",
                        isSelected = sourceFilter == "ALL",
                        onClick = { viewModel.setMessageSourceFilter("ALL") }
                    )
                    FilterPill(
                        label = "SMS",
                        isSelected = sourceFilter == "SMS",
                        onClick = { viewModel.setMessageSourceFilter("SMS") }
                    )
                    FilterPill(
                        label = "WhatsApp",
                        isSelected = sourceFilter == "WHATSAPP",
                        onClick = { viewModel.setMessageSourceFilter("WHATSAPP") }
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showConfigDialog = true }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = TogetherPrimaryGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Filter Numbers",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TogetherPrimaryGreen
                    )
                }
            }
        }

        // Message Feed or Empty State
        if (filteredMessages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TogetherEmptyState(
                    title = if (isSharingPaused) "Sharing Paused" else "Nothing shared yet",
                    description = if (isSharingPaused) {
                        "Message sharing is temporarily paused in Settings."
                    } else {
                        "Link your WhatsApp with QR code or import a conversation to share."
                    },
                    icon = Icons.Outlined.MailOutline,
                    actionText = if (!isSharingPaused) "Link WhatsApp" else null,
                    onActionClick = { showQrLinkDialog = true }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(filteredMessages, key = { it.id }) { message ->
                    val isCurrentUser = message.senderId == activePartnerId
                    TogetherMessageBubble(
                        message = message,
                        isCurrentUser = isCurrentUser
                    )
                }
            }
        }

        // Clean message composer bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Source:",
                    fontSize = 11.sp,
                    color = TogetherSecondaryText
                )
                Spacer(modifier = Modifier.width(8.dp))
                SourceTogglePill(
                    label = "SMS",
                    isSelected = selectedSendSource == SourceType.SMS,
                    onClick = { selectedSendSource = SourceType.SMS }
                )
                Spacer(modifier = Modifier.width(6.dp))
                SourceTogglePill(
                    label = "WhatsApp",
                    isSelected = selectedSendSource == SourceType.WHATSAPP,
                    onClick = { selectedSendSource = SourceType.WHATSAPP }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newMessageText,
                    onValueChange = { newMessageText = it },
                    placeholder = {
                        Text(
                            "Type a message to share...",
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = TogetherPrimaryGreen
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("compose_message_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(TogetherPrimaryGreen)
                        .clickable {
                            if (newMessageText.isNotBlank()) {
                                viewModel.sendMessage(newMessageText, selectedSendSource)
                                newMessageText = ""
                            }
                        }
                        .testTag("send_message_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Full WhatsApp QR Code Link Dialog
    if (showQrLinkDialog) {
        Dialog(onDismissRequest = { showQrLinkDialog = false }) {
            WhatsAppLinkingCard(
                isLinked = isWhatsAppLinked,
                linkedDeviceName = linkedDeviceName,
                onSimulateScanLink = {
                    viewModel.linkWhatsApp()
                    showQrLinkDialog = false
                    showConfigDialog = true // Prompt them to customize what to share immediately!
                },
                onUnlink = {
                    viewModel.unlinkWhatsApp()
                    showQrLinkDialog = false
                },
                onManageSharing = {
                    showQrLinkDialog = false
                    showConfigDialog = true
                }
            )
        }
    }

    // Selective Sharing / Number Configuration Dialog
    if (showConfigDialog) {
        ConversationConfigDialog(
            whatsAppChats = whatsAppChats,
            smsContacts = smsContacts,
            onToggleWhatsAppChat = { chatId -> viewModel.toggleWhatsAppChatSharing(chatId) },
            onToggleSmsContact = { phone -> viewModel.toggleSmsContactSharing(phone) },
            onUpdateSmsContact = { oldNum, newName, newNum -> viewModel.updateSmsContact(oldNum, newName, newNum) },
            onDismiss = { showConfigDialog = false }
        )
    }

    if (showImportDialog) {
        ImportChatDialog(
            onDismiss = { showImportDialog = false },
            onImport = { rawText ->
                viewModel.importWhatsAppChat(rawText)
                showImportDialog = false
            }
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TogetherPrimaryText else MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                if (isSelected) TogetherPrimaryText else TogetherBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = if (isSelected) Color.White else TogetherSecondaryText
        )
    }
}

@Composable
private fun SourceTogglePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) TogetherSoftGreen else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) TogetherPrimaryGreen else TogetherBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color(0xFF327A00) else TogetherSecondaryText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportChatDialog(
    onDismiss: () -> Unit,
    onImport: (String) -> Unit
) {
    var rawText by remember { mutableStateOf("") }

    val sampleChat = """
11/10/26, 19:15 - Alex: I found a quiet cabin for our weekend getaway.
11/10/26, 19:16 - Sam: That looks beautiful! Let's book it for the 24th.
11/10/26, 19:18 - Alex: Perfect, reserving it right away.
    """.trimIndent()

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Import WhatsApp Conversation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TogetherPrimaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Paste text from a WhatsApp chat export (.txt) or use sample messages.",
                    fontSize = 13.sp,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = rawText,
                    onValueChange = { rawText = it },
                    placeholder = {
                        Text(
                            "Paste exported conversation here...",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("chat_import_text_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                TogetherButton(
                    text = "Load Sample Trip Chat",
                    onClick = { rawText = sampleChat },
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 38.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                TogetherButton(
                    text = "Import Conversation",
                    onClick = {
                        if (rawText.isNotBlank()) {
                            onImport(rawText)
                        }
                    },
                    variant = TogetherButtonVariant.PRIMARY_GREEN,
                    height = 44.dp,
                    enabled = rawText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TogetherButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 38.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
