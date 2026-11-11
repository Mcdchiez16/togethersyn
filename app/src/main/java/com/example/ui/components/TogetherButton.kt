package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class TogetherButtonVariant {
    PRIMARY_GREEN,
    WARM_PINK,
    MUTED_PURPLE,
    BLUE,
    OUTLINED_NEUTRAL
}

@Composable
fun TogetherButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: TogetherButtonVariant = TogetherButtonVariant.PRIMARY_GREEN,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    testTag: String = "together_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Tactile button: gentle 2.5dp solid shadow
    val shadowHeight = 2.5.dp
    val animatedPressOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) shadowHeight else 0.dp,
        animationSpec = tween(durationMillis = 150),
        label = "button_offset"
    )

    val (faceColor, shadowColor, textColor) = when (variant) {
        TogetherButtonVariant.PRIMARY_GREEN -> Triple(TogetherPrimaryGreen, TogetherDeepGreen, Color.White)
        TogetherButtonVariant.WARM_PINK -> Triple(TogetherWarmPink, TogetherDeepPink, Color.White)
        TogetherButtonVariant.MUTED_PURPLE -> Triple(TogetherMutedPurple, TogetherDeepPurple, Color.White)
        TogetherButtonVariant.BLUE -> Triple(TogetherSkyBlue, Color(0xFF1F6AB0), Color.White)
        TogetherButtonVariant.OUTLINED_NEUTRAL -> Triple(TogetherCardBg, Color(0xFFDEDEC), TogetherPrimaryText)
    }

    val cornerRadius = 14.dp
    val buttonShape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .height(height + shadowHeight)
    ) {
        // Bottom solid shadow layer (NO GRADIENTS)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = shadowHeight)
                .clip(buttonShape)
                .background(if (enabled) shadowColor else Color(0xFFDFDFDC))
        )

        // Top button face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = animatedPressOffset)
                .clip(buttonShape)
                .background(if (enabled) faceColor else Color(0xFFEBEBEA))
                .then(
                    if (variant == TogetherButtonVariant.OUTLINED_NEUTRAL) {
                        Modifier.border(1.5.dp, if (enabled) TogetherBorder else Color(0xFFDCDCDA), buttonShape)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled) textColor else TogetherSecondaryText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = if (enabled) textColor else TogetherSecondaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}
