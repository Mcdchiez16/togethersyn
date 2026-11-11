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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MessageEntity
import com.example.data.model.SourceType
import com.example.data.model.WhatsAppChatSummary
import com.example.ui.components.ConversationConfigDialog
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.TogetherMessageBubble
import com.example.ui.components.WhatsAppLinkingCard
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.viewmodel.TogetherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppScreen(
    viewModel: TogetherViewModel,
    modifier: Modifier = Modifier
) {
    val isWhatsAppLinked by viewModel.isWhatsAppLinked.collectAsState()
    val linkedDeviceName by viewModel.linkedDeviceName.collectAsState()
    val whatsAppChats by viewModel.whatsAppChats.collectAsState()
    val smsContacts by viewModel.smsContacts.collectAsState()
    val allMessages by viewModel.messages.collectAsState()
    val activePartnerId by viewModel.activePartnerId.collectAsState()
    val selectedChatId by viewModel.selectedWhatsAppChatId.collectAsState()

    var showQrDialog by remember { mutableStateOf(false) }
    var showConfigDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var chatMessageText by remember { mutableStateOf("") }

    val activeChat = whatsAppChats.find { it.id == selectedChatId }

    if (activeChat != null) {
        // Individual WhatsApp Conversation Detail Thread View
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Thread Header with Contact Details & Origin
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.selectWhatsAppChat(null) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TogetherPrimaryText
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(TogetherSoftGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activeChat.avatarInitials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF287D17)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = activeChat.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TogetherPrimaryText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Companion",
                            tint = Color(0xFF1E7E34),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = "${activeChat.phoneNumber} • ${activeChat.originDevice}",
                        fontSize = 11.sp,
                        color = TogetherSecondaryText
                    )
                }
            }

            // Messages belonging to this WhatsApp chat or all shared WhatsApp messages
            val chatMessages = allMessages.filter { it.sourceType == SourceType.WHATSAPP }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(chatMessages, key = { it.id }) { message ->
                    val isCurrentUser = message.senderId == activePartnerId
                    TogetherMessageBubble(
                        message = message,
                        isCurrentUser = isCurrentUser
                    )
                }
            }

            // WhatsApp Message Composer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = chatMessageText,
                    onValueChange = { chatMessageText = it },
                    placeholder = { Text("Message via WhatsApp...") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(TogetherPrimaryGreen)
                        .clickable {
                            if (chatMessageText.isNotBlank()) {
                                viewModel.sendMessage(
                                    text = chatMessageText,
                                    sourceType = SourceType.WHATSAPP,
                                    recipientContact = activeChat.name,
                                    contactNumber = activeChat.phoneNumber
                                )
                                chatMessageText = ""
                            }
                        },
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
    } else {
        // WhatsApp Chats List & Linking Dashboard
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WhatsApp",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Linked Web Companion & Contacts",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TogetherSecondaryText
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TogetherButton(
                            text = if (isWhatsAppLinked) "Filter" else "Link QR",
                            onClick = {
                                if (isWhatsAppLinked) showConfigDialog = true else showQrDialog = true
                            },
                            variant = if (isWhatsAppLinked) TogetherButtonVariant.OUTLINED_NEUTRAL else TogetherButtonVariant.PRIMARY_GREEN,
                            icon = if (isWhatsAppLinked) Icons.Default.Tune else Icons.Default.QrCodeScanner,
                            height = 38.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // WhatsApp Device Connection Card
                TogetherCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = if (isWhatsAppLinked) Color(0xFFF7FAF6) else TogetherCardBg,
                    borderColor = if (isWhatsAppLinked) Color(0xFFC8EAB5) else TogetherBorder,
                    cornerRadius = 16.dp,
                    onClick = {
                        if (isWhatsAppLinked) showConfigDialog = true else showQrDialog = true
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
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
                                    .background(if (isWhatsAppLinked) TogetherSoftGreen else Color(0xFFEDF2F7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isWhatsAppLinked) Icons.Default.CheckCircle else Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = if (isWhatsAppLinked) "WhatsApp Web Linked" else "Link WhatsApp via QR Code",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TogetherPrimaryText
                                )
                                Text(
                                    text = if (isWhatsAppLinked) (linkedDeviceName ?: "Active Companion Session") else "Scan to view and sync your chats live",
                                    fontSize = 11.sp,
                                    color = if (isWhatsAppLinked) TogetherPrimaryGreen else TogetherSecondaryText
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isWhatsAppLinked) TogetherSoftGreen else Color(0xFFF2F2F0))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isWhatsAppLinked) "CONNECTED" else "PAIR QR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isWhatsAppLinked) Color(0xFF287D17) else TogetherSecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search WhatsApp contacts & numbers...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // WhatsApp Chats List
            val filteredChats = whatsAppChats.filter { chat ->
                searchQuery.isBlank() ||
                        chat.name.contains(searchQuery, ignoreCase = true) ||
                        chat.phoneNumber.contains(searchQuery) ||
                        chat.lastMessage.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredChats, key = { it.id }) { chat ->
                    WhatsAppChatRowCard(
                        chat = chat,
                        onClick = { viewModel.selectWhatsAppChat(chat.id) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // QR Code Dialog
    if (showQrDialog) {
        Dialog(onDismissRequest = { showQrDialog = false }) {
            WhatsAppLinkingCard(
                isLinked = isWhatsAppLinked,
                linkedDeviceName = linkedDeviceName,
                onSimulateScanLink = {
                    viewModel.linkWhatsApp()
                    showQrDialog = false
                    showConfigDialog = true
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

    // Selective Sharing Config Dialog
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
}

@Composable
fun WhatsAppChatRowCard(
    chat: WhatsAppChatSummary,
    onClick: () -> Unit
) {
    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormatter.format(Date(chat.timestamp))

    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 14.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (chat.isGroup) Color(0xFFEDF2F7) else TogetherSoftGreen),
                contentAlignment = Alignment.Center
            ) {
                if (chat.isGroup) {
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = null,
                        tint = Color(0xFF4A5568),
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = chat.avatarInitials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF287D17)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TogetherPrimaryText
                    )
                    Text(
                        text = formattedTime,
                        fontSize = 11.sp,
                        color = TogetherSecondaryText
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Contact details line: shows phone number & origin device
                Text(
                    text = "${chat.phoneNumber} • ${chat.originDevice}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF287D17)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = chat.lastMessage,
                    fontSize = 12.sp,
                    color = TogetherSecondaryText,
                    maxLines = 1
                )
            }
        }
    }
}
