package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherWarmPink

@Composable
fun TogetherCelebrationDialog(
    title: String,
    subtitle: String,
    isRomantic: Boolean = false,
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.85f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .scale(scale.value)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(TogetherCardBg)
                .border(
                    1.5.dp,
                    if (isRomantic) Color(0xFFFFD4DE) else Color(0xFFC8EAB5),
                    RoundedCornerShape(20.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Subtle elegant emblem
                val emblemBg = if (isRomantic) TogetherSoftPink else TogetherSoftGreen
                val emblemColor = if (isRomantic) TogetherWarmPink else TogetherPrimaryGreen

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(emblemBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRomantic) Icons.Default.Favorite else Icons.Default.Check,
                        contentDescription = null,
                        tint = emblemColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    color = TogetherPrimaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    color = TogetherSecondaryText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                TogetherButton(
                    text = "Continue",
                    onClick = onDismiss,
                    variant = if (isRomantic) TogetherButtonVariant.WARM_PINK else TogetherButtonVariant.PRIMARY_GREEN,
                    height = 44.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
