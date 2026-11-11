package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import kotlinx.coroutines.delay

@Composable
fun WhatsAppQrCodeCanvas(
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
    seed: Int = 42
) {
    // Generate deterministic 21x21 QR matrix with standard 7x7 corner finder patterns
    val gridSize = 21
    val matrix = remember(seed) {
        val grid = Array(gridSize) { BooleanArray(gridSize) }

        // Finder pattern helper (7x7)
        fun setFinder(startR: Int, startC: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                    val isCenter = r in 2..4 && c in 2..4
                    grid[startR + r][startC + c] = isBorder || isCenter
                }
            }
        }

        // Top-left, top-right, bottom-left finders
        setFinder(0, 0)
        setFinder(0, gridSize - 7)
        setFinder(gridSize - 7, 0)

        // Timing patterns
        for (i in 8 until gridSize - 8) {
            grid[6][i] = i % 2 == 0
            grid[i][6] = i % 2 == 0
        }

        // Pseudo-random data modules based on seed
        val random = java.util.Random(seed.toLong() * 997L + 12345L)
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val inTopLeft = r < 8 && c < 8
                val inTopRight = r < 8 && c >= gridSize - 8
                val inBottomLeft = r >= gridSize - 8 && c < 8
                val inTiming = (r == 6 || c == 6)
                if (!inTopLeft && !inTopRight && !inBottomLeft && !inTiming) {
                    grid[r][c] = random.nextBoolean()
                }
            }
        }
        grid
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan_offset"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.5.dp, TogetherBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size - 24.dp)) {
            val cellWidth = this.size.width / gridSize
            val cellHeight = this.size.height / gridSize

            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    if (matrix[r][c]) {
                        drawRect(
                            color = Color(0xFF111111),
                            topLeft = Offset(c * cellWidth, r * cellHeight),
                            size = Size(cellWidth * 0.96f, cellHeight * 0.96f)
                        )
                    }
                }
            }

            // Green scanning bar overlay
            val scanY = this.size.height * scanProgress
            drawLine(
                color = TogetherPrimaryGreen,
                start = Offset(0f, scanY),
                end = Offset(this.size.width, scanY),
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun WhatsAppLinkingCard(
    isLinked: Boolean,
    linkedDeviceName: String?,
    onSimulateScanLink: () -> Unit,
    onUnlink: () -> Unit,
    onManageSharing: () -> Unit,
    modifier: Modifier = Modifier
) {
    var countdown by remember { mutableIntStateOf(45) }
    var qrSeed by remember { mutableIntStateOf(101) }

    LaunchedEffect(isLinked) {
        while (!isLinked) {
            delay(1000)
            if (countdown > 1) {
                countdown--
            } else {
                countdown = 45
                qrSeed += 7
            }
        }
    }

    TogetherCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = if (isLinked) Color(0xFFC8EAB5) else TogetherBorder,
        cornerRadius = 18.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isLinked) TogetherSoftGreen else Color(0xFFEDF2F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLinked) Icons.Default.CheckCircle else Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = if (isLinked) TogetherPrimaryGreen else TogetherPrimaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isLinked) "WhatsApp Linked" else "Link WhatsApp Web",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TogetherPrimaryText
                        )
                        Text(
                            text = if (isLinked) (linkedDeviceName ?: "Active Companion Device") else "Scan QR code with WhatsApp",
                            fontSize = 12.sp,
                            color = if (isLinked) TogetherPrimaryGreen else TogetherSecondaryText
                        )
                    }
                }

                if (isLinked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TogetherSoftGreen)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF287D17)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isLinked) {
                // QR Scanner View
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WhatsAppQrCodeCanvas(seed = qrSeed)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "QR code refreshes in ${countdown}s",
                            fontSize = 12.sp,
                            color = TogetherSecondaryText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = TogetherSecondaryText,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    countdown = 45
                                    qrSeed += 3
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step-by-step instructions
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF9F9F8), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        InstructionRow(step = "1", text = "Open WhatsApp on your mobile phone")
                        InstructionRow(step = "2", text = "Tap Menu (⋮) or Settings > Linked Devices")
                        InstructionRow(step = "3", text = "Point phone here or tap Link for testing")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Immediate Live Test Link action
                    TogetherButton(
                        text = "Simulate Scan & Link Device",
                        onClick = onSimulateScanLink,
                        variant = TogetherButtonVariant.PRIMARY_GREEN,
                        icon = Icons.Default.QrCodeScanner,
                        height = 44.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // Connected state with management prompt
                Text(
                    text = "Your WhatsApp account is successfully connected. You can choose exactly which conversations and numbers you wish to share with your partner.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TogetherButton(
                        text = "Manage Sharing",
                        onClick = onManageSharing,
                        variant = TogetherButtonVariant.PRIMARY_GREEN,
                        height = 40.dp,
                        modifier = Modifier.weight(1f)
                    )

                    TogetherButton(
                        text = "Unlink",
                        onClick = onUnlink,
                        variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                        height = 40.dp,
                        modifier = Modifier.weight(0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InstructionRow(step: String, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(TogetherPrimaryGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = TogetherPrimaryText
        )
    }
}
