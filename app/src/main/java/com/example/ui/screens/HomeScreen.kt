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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SourceType
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.TogetherConnectedAvatars
import com.example.ui.components.TogetherStreakBadge
import com.example.ui.components.TogetherTab
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSkyBlue
import com.example.ui.theme.TogetherSoftBlue
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherSoftPurple
import com.example.ui.theme.TogetherWarmPink
import com.example.ui.viewmodel.TogetherViewModel

@Composable
fun HomeScreen(
    viewModel: TogetherViewModel,
    onNavigateToTab: (TogetherTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val activePartnerId by viewModel.activePartnerId.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val calls by viewModel.calls.collectAsState()
    val moments by viewModel.moments.collectAsState()

    val currentPartnerName = if (activePartnerId == "ALEX") "Alex" else "Sam"
    val otherPartnerName = if (activePartnerId == "ALEX") "Sam" else "Alex"

    val whatsappCount = messages.count { it.sourceType == SourceType.WHATSAPP }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Top Bar: Partner Status & Streak Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, TogetherBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(TogetherPrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Paired with $otherPartnerName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            TogetherStreakBadge(streakDays = streakDays)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Editorial Header
        Text(
            text = "Good morning, $currentPartnerName",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "A little closer, every day.",
            style = MaterialTheme.typography.bodyMedium,
            color = TogetherSecondaryText
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Partner Connection Card
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TogetherConnectedAvatars(
                    partner1Name = currentPartnerName,
                    partner2Name = otherPartnerName,
                    avatarSize = 56.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "You're Connected",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Your private shared space is synchronized and secure.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                TogetherButton(
                    text = "Write a Love Letter",
                    onClick = { onNavigateToTab(TogetherTab.OUR_WORLD) },
                    variant = TogetherButtonVariant.WARM_PINK,
                    icon = Icons.Default.Favorite,
                    height = 42.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Shared Spaces",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Feature cards: Messages, Calls, WhatsApp, Our World
        FeatureCard(
            title = "Messages",
            subtitle = "${messages.size} conversations synced",
            icon = Icons.Outlined.MailOutline,
            iconColor = TogetherPrimaryGreen,
            iconBg = TogetherSoftGreen,
            onClick = { onNavigateToTab(TogetherTab.MESSAGES) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        FeatureCard(
            title = "Calls",
            subtitle = "${calls.size} call records in history",
            icon = Icons.Outlined.Call,
            iconColor = TogetherSkyBlue,
            iconBg = TogetherSoftBlue,
            onClick = { onNavigateToTab(TogetherTab.CALLS) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        val isWhatsAppLinked by viewModel.isWhatsAppLinked.collectAsState()

        FeatureCard(
            title = "WhatsApp",
            subtitle = if (isWhatsAppLinked) "Linked • $whatsappCount chats in live sync" else "Link with QR code to sync chats",
            icon = Icons.Outlined.ChatBubbleOutline,
            iconColor = Color(0xFF1E7E34),
            iconBg = Color(0xFFEAF7EE),
            onClick = { onNavigateToTab(TogetherTab.WHATSAPP) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        FeatureCard(
            title = "Our World",
            subtitle = "${moments.size} memories, love letters & playlist",
            icon = Icons.Outlined.FavoriteBorder,
            iconColor = TogetherWarmPink,
            iconBg = TogetherSoftPink,
            onClick = { onNavigateToTab(TogetherTab.OUR_WORLD) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    onClick: () -> Unit
) {
    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 16.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TogetherPrimaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TogetherSecondaryText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFB0B0AC),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
