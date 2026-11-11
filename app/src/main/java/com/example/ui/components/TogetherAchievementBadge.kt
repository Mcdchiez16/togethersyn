package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MilestoneItem
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherGoldYellow
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherSoftYellow
import com.example.ui.theme.TogetherWarmPink

@Composable
fun TogetherStreakBadge(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TogetherSoftGreen)
            .border(1.dp, Color(0xFFC8EAB5), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Whatshot,
                contentDescription = null,
                tint = TogetherPrimaryGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$streakDays days connected",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFF327A00)
            )
        }
    }
}

@Composable
fun TogetherMilestoneCard(
    milestone: MilestoneItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = if (milestone.isUnlocked) TogetherCardBg else Color(0xFFF9F9F8)
    val cardBorder = if (milestone.isUnlocked) TogetherBorder else Color(0xFFE5E5E3)

    val milestoneIcon: ImageVector = when (milestone.iconName) {
        "favorite" -> Icons.Default.Favorite
        "timeline" -> Icons.Default.Timeline
        "mail" -> Icons.Default.Mail
        "chat" -> Icons.Outlined.ChatBubbleOutline
        else -> Icons.Default.DateRange
    }

    TogetherCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = cardBg,
        borderColor = cardBorder,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconBg = if (milestone.isUnlocked) {
                try {
                    Color(android.graphics.Color.parseColor(milestone.colorHex)).copy(alpha = 0.14f)
                } catch (_: Exception) {
                    TogetherSoftGreen
                }
            } else {
                Color(0xFFEEEEEE)
            }

            val iconTint = if (milestone.isUnlocked) {
                try {
                    Color(android.graphics.Color.parseColor(milestone.colorHex))
                } catch (_: Exception) {
                    TogetherPrimaryGreen
                }
            } else {
                TogetherSecondaryText
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (milestone.isUnlocked) milestoneIcon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = milestone.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (milestone.isUnlocked) MaterialTheme.colorScheme.onSurface else TogetherSecondaryText
                )
                Text(
                    text = milestone.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TogetherSecondaryText
                )
            }

            if (milestone.isUnlocked) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TogetherSoftGreen)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Unlocked",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TogetherPrimaryGreen
                    )
                }
            }
        }
    }
}
