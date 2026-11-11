package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.SmsContactFilter
import com.example.data.model.WhatsAppChatSummary
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationConfigDialog(
    whatsAppChats: List<WhatsAppChatSummary>,
    smsContacts: List<SmsContactFilter>,
    onToggleWhatsAppChat: (String) -> Unit,
    onToggleSmsContact: (String) -> Unit,
    onUpdateSmsContact: (String, String, String) -> Unit, // (oldNumber, newName, newNumber)
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableStateOf("WHATSAPP") } // "WHATSAPP" or "SMS"
    var editingContact by remember { mutableStateOf<SmsContactFilter?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Selective Sharing Controls",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TogetherPrimaryText
                        )
                        Text(
                            text = "Choose which chats and numbers to share live",
                            fontSize = 12.sp,
                            color = TogetherSecondaryText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TogetherSecondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Toggle tabs: WhatsApp Chats vs SMS Numbers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF2F2F0), RoundedCornerShape(10.dp))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (activeTab == "WHATSAPP") Color.White else Color.Transparent)
                            .clickable { activeTab = "WHATSAPP" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = null,
                                tint = if (activeTab == "WHATSAPP") TogetherPrimaryGreen else TogetherSecondaryText,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp Chats",
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == "WHATSAPP") FontWeight.SemiBold else FontWeight.Normal,
                                color = if (activeTab == "WHATSAPP") TogetherPrimaryText else TogetherSecondaryText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (activeTab == "SMS") Color.White else Color.Transparent)
                            .clickable { activeTab = "SMS" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.MailOutline,
                                contentDescription = null,
                                tint = if (activeTab == "SMS") TogetherPrimaryGreen else TogetherSecondaryText,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SMS Numbers",
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == "SMS") FontWeight.SemiBold else FontWeight.Normal,
                                color = if (activeTab == "SMS") TogetherPrimaryText else TogetherSecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (activeTab == "WHATSAPP") {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(whatsAppChats, key = { it.id }) { chat ->
                            WhatsAppChatSharingRow(
                                chat = chat,
                                onToggle = { onToggleWhatsAppChat(chat.id) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(smsContacts, key = { it.phoneNumber }) { contact ->
                            SmsContactSharingRow(
                                contact = contact,
                                onToggle = { onToggleSmsContact(contact.phoneNumber) },
                                onEdit = { editingContact = contact }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                TogetherButton(
                    text = "Apply Sharing Preferences",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.PRIMARY_GREEN,
                    height = 42.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Modal to Edit SMS Contact details (change name, change number)
    editingContact?.let { contact ->
        EditSmsContactDialog(
            contact = contact,
            onDismiss = { editingContact = null },
            onSave = { newName, newNumber ->
                onUpdateSmsContact(contact.phoneNumber, newName, newNumber)
                editingContact = null
            }
        )
    }
}

@Composable
private fun WhatsAppChatSharingRow(
    chat: WhatsAppChatSummary,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (chat.isShared) Color(0xFFF7FAF6) else Color(0xFFFBFBFB))
            .border(1.dp, if (chat.isShared) Color(0xFFD6ECCB) else TogetherBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
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
                    .background(if (chat.isShared) TogetherSoftGreen else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.avatarInitials,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (chat.isShared) Color(0xFF287D17) else Color(0xFF4A5568)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = chat.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TogetherPrimaryText
                    )
                    if (chat.isGroup) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(Group)",
                            fontSize = 10.sp,
                            color = TogetherSecondaryText
                        )
                    }
                }
                Text(
                    text = chat.lastMessage,
                    fontSize = 11.sp,
                    color = TogetherSecondaryText,
                    maxLines = 1
                )
            }
        }

        Switch(
            checked = chat.isShared,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TogetherPrimaryGreen
            )
        )
    }
}

@Composable
private fun SmsContactSharingRow(
    contact: SmsContactFilter,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (contact.isShared) Color(0xFFF7FAF6) else Color(0xFFFBFBFB))
            .border(1.dp, if (contact.isShared) Color(0xFFD6ECCB) else TogetherBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
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
                    .background(TogetherSoftGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = TogetherPrimaryGreen,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.contactName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TogetherPrimaryText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Contact",
                        tint = TogetherSecondaryText,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable(onClick = onEdit)
                    )
                }
                Text(
                    text = contact.phoneNumber,
                    fontSize = 11.sp,
                    color = TogetherSecondaryText
                )
            }
        }

        Switch(
            checked = contact.isShared,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TogetherPrimaryGreen
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSmsContactDialog(
    contact: SmsContactFilter,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(contact.contactName) }
    var number by remember { mutableStateOf(contact.phoneNumber) }

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 18.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Edit SMS Contact Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TogetherPrimaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Change the name or phone number displayed in shared logs",
                    fontSize = 12.sp,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Contact Name") },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Phone Number") },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                TogetherButton(
                    text = "Save Changes",
                    onClick = {
                        if (name.isNotBlank() && number.isNotBlank()) {
                            onSave(name, number)
                        }
                    },
                    variant = TogetherButtonVariant.PRIMARY_GREEN,
                    height = 40.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TogetherButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 36.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
