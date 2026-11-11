package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg

@Composable
fun TogetherCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    bottomShadowColor: Color = borderColor,
    cornerRadius: Dp = 18.dp,
    bottomShadowHeight: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    testTag: String = "together_card",
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) bottomShadowHeight else 0.dp,
        animationSpec = tween(durationMillis = 120),
        label = "card_press"
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .testTag(testTag)
            .padding(bottom = bottomShadowHeight)
    ) {
        // Bottom solid shadow (NO GRADIENTS)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = bottomShadowHeight)
                .clip(shape)
                .background(bottomShadowColor)
        )

        // Card Face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = animatedOffset)
                .clip(shape)
                .background(backgroundColor)
                .border(1.2.dp, borderColor, shape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else {
                        Modifier
                    }
                ),
            content = content
        )
    }
}
