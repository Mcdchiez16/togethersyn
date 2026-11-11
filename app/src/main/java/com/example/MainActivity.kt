package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.model.AppThemeMode
import com.example.ui.components.TogetherBottomNavigation
import com.example.ui.components.TogetherCelebrationDialog
import com.example.ui.components.TogetherTab
import com.example.ui.screens.CallsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OurWorldScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WhatsAppScreen
import com.example.ui.theme.TogetherSyncTheme
import com.example.ui.viewmodel.TogetherViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private val viewModel: TogetherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIncomingSharedIntent(intent)

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemDark
            }

            TogetherSyncTheme(darkTheme = isDark) {
                TogetherSyncApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingSharedIntent(intent)
    }

    private fun handleIncomingSharedIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_SEND) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!text.isNullOrBlank()) {
                viewModel.importWhatsAppChat(text)
            } else {
                val streamUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                if (streamUri != null) {
                    try {
                        contentResolver.openInputStream(streamUri)?.use { stream ->
                            val reader = BufferedReader(InputStreamReader(stream))
                            val content = reader.readText()
                            if (content.isNotBlank()) {
                                viewModel.importWhatsAppChat(content)
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }
}

@Composable
fun TogetherSyncApp(viewModel: TogetherViewModel) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val celebration by viewModel.celebration.collectAsState()
    var currentTab by remember { mutableStateOf(TogetherTab.HOME) }

    // Android back navigation handler: if not on Home, back key returns to Home
    BackHandler(enabled = currentTab != TogetherTab.HOME) {
        currentTab = TogetherTab.HOME
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (!isOnboardingCompleted) {
            OnboardingScreen(
                onFinished = { viewModel.completeOnboarding() }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    TogetherBottomNavigation(
                        selectedTab = currentTab,
                        onTabSelected = { currentTab = it }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_content_transition"
                    ) { tab ->
                        when (tab) {
                            TogetherTab.HOME -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToTab = { currentTab = it }
                            )
                            TogetherTab.MESSAGES -> MessagesScreen(viewModel = viewModel)
                            TogetherTab.WHATSAPP -> WhatsAppScreen(viewModel = viewModel)
                            TogetherTab.CALLS -> CallsScreen(viewModel = viewModel)
                            TogetherTab.OUR_WORLD -> OurWorldScreen(viewModel = viewModel)
                            TogetherTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }

        // Animated celebration dialog overlay
        celebration?.let { info ->
            TogetherCelebrationDialog(
                title = info.title,
                subtitle = info.subtitle,
                isRomantic = info.isRomantic,
                onDismiss = { viewModel.dismissCelebration() }
            )
        }
    }
}
