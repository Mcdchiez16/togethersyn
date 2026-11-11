package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TogetherDarkBg
import com.example.ui.theme.TogetherDarkCard
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen

enum class TogetherTab(
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    MESSAGES("Messages", Icons.Filled.Mail, Icons.Outlined.MailOutline),
    WHATSAPP("WhatsApp", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    CALLS("Calls", Icons.Filled.Call, Icons.Outlined.Call),
    OUR_WORLD("Our World", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

val primaryBottomTabs = listOf(
    TogetherTab.HOME,
    TogetherTab.MESSAGES,
    TogetherTab.WHATSAPP,
    TogetherTab.OUR_WORLD,
    TogetherTab.SETTINGS
)

@Composable
fun TogetherBottomNavigation(
    selectedTab: TogetherTab,
    onTabSelected: (TogetherTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background == TogetherDarkBg
    val navBg = if (isDark) TogetherDarkCard else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(navBg)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            primaryBottomTabs.forEach { tab ->
                val isSelected = tab == selectedTab

                val pillBg by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isDark) Color(0xFF223126) else TogetherSoftGreen
                    } else Color.Transparent,
                    animationSpec = tween(durationMillis = 180),
                    label = "tab_pill_bg"
                )

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) TogetherPrimaryGreen else TogetherSecondaryText,
                    animationSpec = tween(durationMillis = 180),
                    label = "tab_content_color"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(pillBg)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("tab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                            contentDescription = tab.label,
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            color = contentColor,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
