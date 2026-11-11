package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen

@Composable
fun TogetherPermissionCard(
    title: String,
    description: String,
    permissionStatus: String,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    TogetherCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TogetherSoftGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TogetherPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TogetherPrimaryText
                    )
                    Text(
                        text = "Status: $permissionStatus",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TogetherSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Normal,
                color = TogetherSecondaryText
            )

            Spacer(modifier = Modifier.height(14.dp))

            TogetherButton(
                text = "Request Device Permission",
                onClick = onRequestPermission,
                variant = TogetherButtonVariant.PRIMARY_GREEN,
                height = 42.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
