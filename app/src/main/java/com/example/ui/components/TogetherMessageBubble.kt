package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageEntity
import com.example.data.model.SourceType
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TogetherMessageBubble(
    message: MessageEntity,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    val bubbleShape = if (isCurrentUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    val bubbleBg = if (isCurrentUser) TogetherSoftGreen else TogetherCardBg
    val bubbleBorder = if (isCurrentUser) Color(0xFFC8EAB5) else TogetherBorder

    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormatter.format(Date(message.timestamp))

    val isWhatsApp = message.sourceType == SourceType.WHATSAPP
    val tagBg = if (isWhatsApp) Color(0xFFE6F7EC) else Color(0xFFEDF2F7)
    val tagColor = if (isWhatsApp) Color(0xFF1E7E34) else Color(0xFF4A5568)
    val tagIcon = if (isWhatsApp) Icons.Outlined.ChatBubbleOutline else Icons.Outlined.MailOutline
    val tagText = if (isWhatsApp) "WhatsApp" else "SMS"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isCurrentUser) {
            TogetherAvatar(
                name = message.senderName,
                size = 32.dp,
                isFirstPartner = false
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            // Contact Details & Origin line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = "${message.senderName} (${message.contactNumber})",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = TogetherPrimaryText
                )
                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(tagBg)
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = tagIcon,
                        contentDescription = null,
                        tint = tagColor,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = tagText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = tagColor
                    )
                }
            }

            // Message Bubble
            Box(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .clip(bubbleShape)
                    .background(bubbleBg)
                    .border(1.dp, bubbleBorder, bubbleShape)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = TogetherPrimaryText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Origin details indicator
                        Text(
                            text = "${message.originDetails} • $formattedTime",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = TogetherSecondaryText
                        )
                        if (isCurrentUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "Delivered",
                                tint = if (isWhatsApp) Color(0xFF1E7E34) else TogetherPrimaryGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            TogetherAvatar(
                name = message.senderName,
                size = 32.dp,
                isFirstPartner = true
            )
        }
    }
}
