package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherWarmPink

@Composable
fun TogetherAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isFirstPartner: Boolean = true
) {
    val initial = name.firstOrNull()?.uppercase() ?: "A"
    val bgColor = if (isFirstPartner) TogetherSoftGreen else TogetherSoftPink
    val textColor = if (isFirstPartner) Color(0xFF327A00) else TogetherWarmPink
    val borderColor = if (isFirstPartner) Color(0xFFC8EAB5) else Color(0xFFFFD4DE)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.42f).sp,
            color = textColor
        )
    }
}

@Composable
fun TogetherConnectedAvatars(
    partner1Name: String = "Alex",
    partner2Name: String = "Sam",
    modifier: Modifier = Modifier,
    avatarSize: Dp = 54.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogetherAvatar(
            name = partner1Name,
            size = avatarSize,
            isFirstPartner = true
        )

        // Small, elegant vector heart icon between the profile avatars
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(TogetherSoftPink)
                .border(1.dp, Color(0xFFFFD4DE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Connected",
                tint = TogetherWarmPink,
                modifier = Modifier.size(14.dp)
            )
        }

        TogetherAvatar(
            name = partner2Name,
            size = avatarSize,
            isFirstPartner = false
        )
    }
}
