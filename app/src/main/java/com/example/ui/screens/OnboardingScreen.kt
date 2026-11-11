package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherProgressBar
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherSoftPurple
import com.example.ui.theme.TogetherWarmPink

data class OnboardingStep(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val buttonText: String
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var stepIndex by remember { mutableIntStateOf(0) }

    val pages = listOf(
        OnboardingStep(
            title = "Better Together",
            description = "A quiet, private space created for two.",
            icon = Icons.Default.Favorite,
            iconBg = TogetherSoftPink,
            iconTint = TogetherWarmPink,
            buttonText = "Continue"
        ),
        OnboardingStep(
            title = "Connect Your Worlds",
            description = "Invite your favorite person to stay in sync throughout the day.",
            icon = Icons.Outlined.PersonAdd,
            iconBg = TogetherSoftGreen,
            iconTint = TogetherPrimaryGreen,
            buttonText = "Continue"
        ),
        OnboardingStep(
            title = "Share Your Way",
            description = "You decide what to share, and when. Privacy and consent come first.",
            icon = Icons.Outlined.Shield,
            iconBg = TogetherSoftPurple,
            iconTint = Color(0xFF7554C0),
            buttonText = "Understood"
        ),
        OnboardingStep(
            title = "Welcome to TogetherSync",
            description = "Your shared world is ready for memories, letters, and conversations.",
            icon = Icons.Outlined.CheckCircle,
            iconBg = TogetherSoftGreen,
            iconTint = TogetherPrimaryGreen,
            buttonText = "Enter Our Space"
        )
    )

    val currentPage = pages[stepIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogetherProgressBar(
                progress = (stepIndex + 1) / 4f,
                modifier = Modifier.weight(1f)
            )

            if (stepIndex < 3) {
                TextButton(
                    onClick = onFinished,
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = "Skip",
                        fontWeight = FontWeight.SemiBold,
                        color = TogetherSecondaryText,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedContent(
            targetState = currentPage,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "onboarding_step"
        ) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(page.iconBg)
                        .border(1.5.dp, TogetherBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = page.iconTint,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = page.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    color = TogetherPrimaryText
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = page.description,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    color = TogetherSecondaryText,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TogetherButton(
            text = currentPage.buttonText,
            onClick = {
                if (stepIndex < 3) {
                    stepIndex++
                } else {
                    onFinished()
                }
            },
            variant = TogetherButtonVariant.PRIMARY_GREEN,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
