package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DrawingPoint
import com.example.data.model.DrawingStroke
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherWarmPink

private data class ColorOption(val hex: String, val color: Color, val name: String)

private val canvasColors = listOf(
    ColorOption("#F26D85", Color(0xFFF26D85), "Pink"),
    ColorOption("#58CC02", Color(0xFF58CC02), "Green"),
    ColorOption("#1CB0F6", Color(0xFF1CB0F6), "Blue"),
    ColorOption("#9274D3", Color(0xFF9274D3), "Purple"),
    ColorOption("#FFC800", Color(0xFFFFC800), "Yellow"),
    ColorOption("#242424", Color(0xFF242424), "Charcoal"),
    ColorOption("#FFFFFF", Color(0xFFFFFFFF), "Eraser")
)

@Composable
fun TogetherDrawingCanvas(
    strokes: List<DrawingStroke>,
    isPartnerDrawing: Boolean,
    onAddStroke: (DrawingStroke) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    onSimulatePartnerDoodle: () -> Unit,
    onSaveToMemories: () -> Unit,
    authorName: String,
    modifier: Modifier = Modifier
) {
    var selectedColorHex by remember { mutableStateOf("#F26D85") }
    var selectedStrokeWidth by remember { mutableFloatStateOf(6f) }
    val currentPoints = remember { mutableStateListOf<DrawingPoint>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.5.dp, TogetherBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        // Status & Connectivity Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(TogetherPrimaryGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live Shared Whiteboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TogetherPrimaryText
                )
            }

            AnimatedVisibility(
                visible = isPartnerDrawing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEAF7E2))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Partner is drawing...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TogetherPrimaryGreen
                    )
                }
            }

            if (!isPartnerDrawing) {
                Text(
                    text = "${strokes.size} strokes",
                    fontSize = 12.sp,
                    color = TogetherSecondaryText,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Live Drawing Surface (White Space)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFCFCFC))
                .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(12.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .pointerInput(selectedColorHex, selectedStrokeWidth) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPoints.clear()
                                currentPoints.add(DrawingPoint(offset.x, offset.y))
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPoints.add(DrawingPoint(change.position.x, change.position.y))
                            },
                            onDragEnd = {
                                if (currentPoints.size > 1) {
                                    val stroke = DrawingStroke(
                                        id = "stroke_${System.currentTimeMillis()}",
                                        points = currentPoints.toList(),
                                        colorHex = selectedColorHex,
                                        strokeWidth = selectedStrokeWidth,
                                        author = authorName
                                    )
                                    onAddStroke(stroke)
                                }
                                currentPoints.clear()
                            },
                            onDragCancel = {
                                currentPoints.clear()
                            }
                        )
                    }
            ) {
                // Draw existing saved strokes
                strokes.forEach { stroke ->
                    val strokeColor = if (stroke.colorHex.equals("#FFFFFF", ignoreCase = true)) {
                        Color(0xFFFCFCFC) // eraser matches surface
                    } else {
                        try {
                            Color(android.graphics.Color.parseColor(stroke.colorHex))
                        } catch (e: Exception) {
                            TogetherPrimaryText
                        }
                    }

                    if (stroke.points.size > 1) {
                        val path = Path()
                        path.moveTo(stroke.points.first().x, stroke.points.first().y)
                        for (i in 1 until stroke.points.size) {
                            path.lineTo(stroke.points[i].x, stroke.points[i].y)
                        }
                        drawPath(
                            path = path,
                            color = strokeColor,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (stroke.points.size == 1) {
                        drawCircle(
                            color = strokeColor,
                            radius = stroke.strokeWidth / 2f,
                            center = Offset(stroke.points.first().x, stroke.points.first().y)
                        )
                    }
                }

                // Draw currently in-progress active stroke
                if (currentPoints.size > 1) {
                    val activeColor = if (selectedColorHex.equals("#FFFFFF", ignoreCase = true)) {
                        Color(0xFFFCFCFC)
                    } else {
                        try {
                            Color(android.graphics.Color.parseColor(selectedColorHex))
                        } catch (e: Exception) {
                            TogetherPrimaryText
                        }
                    }
                    val path = Path()
                    path.moveTo(currentPoints.first().x, currentPoints.first().y)
                    for (i in 1 until currentPoints.size) {
                        path.lineTo(currentPoints[i].x, currentPoints[i].y)
                    }
                    drawPath(
                        path = path,
                        color = activeColor,
                        style = Stroke(
                            width = selectedStrokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }

            // Prompt when canvas is completely fresh
            if (strokes.isEmpty() && currentPoints.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(290.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color(0xFFC4C4C2),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Draw something together",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFA5A5A2)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Color Palette & Eraser Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                canvasColors.forEach { option ->
                    val isSelected = selectedColorHex.equals(option.hex, ignoreCase = true)
                    val isEraser = option.hex.equals("#FFFFFF", ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(option.color)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) TogetherPrimaryText else Color(0xFFD4D4D0),
                                shape = CircleShape
                            )
                            .clickable { selectedColorHex = option.hex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isEraser) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Eraser",
                                tint = TogetherPrimaryText,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Stroke Thickness Selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StrokeWidthChip(
                    label = "S",
                    isSelected = selectedStrokeWidth == 3f,
                    onClick = { selectedStrokeWidth = 3f }
                )
                StrokeWidthChip(
                    label = "M",
                    isSelected = selectedStrokeWidth == 6f,
                    onClick = { selectedStrokeWidth = 6f }
                )
                StrokeWidthChip(
                    label = "L",
                    isSelected = selectedStrokeWidth == 12f,
                    onClick = { selectedStrokeWidth = 12f }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TogetherButton(
                text = "Undo",
                onClick = onUndo,
                variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                icon = Icons.AutoMirrored.Filled.Undo,
                height = 36.dp,
                modifier = Modifier.weight(1f)
            )

            TogetherButton(
                text = "Clear",
                onClick = onClear,
                variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                icon = Icons.Default.Clear,
                height = 36.dp,
                modifier = Modifier.weight(1f)
            )

            TogetherButton(
                text = "Partner Doodle",
                onClick = onSimulatePartnerDoodle,
                variant = TogetherButtonVariant.PRIMARY_GREEN,
                icon = Icons.Default.AutoAwesome,
                height = 36.dp,
                modifier = Modifier.weight(1.3f)
            )

            TogetherButton(
                text = "Save",
                onClick = onSaveToMemories,
                variant = TogetherButtonVariant.WARM_PINK,
                icon = Icons.Default.Bookmark,
                height = 36.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StrokeWidthChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) TogetherPrimaryText else Color(0xFFF2F2F0))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TogetherSecondaryText
        )
    }
}
