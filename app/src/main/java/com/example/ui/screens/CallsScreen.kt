package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CallDirection
import com.example.data.model.CallEntity
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.TogetherEmptyState
import com.example.ui.components.TogetherPermissionCard
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSkyBlue
import com.example.ui.theme.TogetherSoftBlue
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherWarmPink
import com.example.ui.viewmodel.TogetherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CallsScreen(
    viewModel: TogetherViewModel,
    modifier: Modifier = Modifier
) {
    val allCalls by viewModel.calls.collectAsState()
    val directionFilter by viewModel.callDirectionFilter.collectAsState()
    val isSharingCalls by viewModel.isSharingCalls.collectAsState()
    val isSharingPaused by viewModel.isSharingPaused.collectAsState()

    val filteredCalls = allCalls.filter { call ->
        val matchesDirection = when (directionFilter) {
            "INCOMING" -> call.direction == CallDirection.INCOMING
            "OUTGOING" -> call.direction == CallDirection.OUTGOING
            "MISSED" -> call.direction == CallDirection.MISSED
            else -> true
        }
        val matchesPrivacy = !isSharingPaused && isSharingCalls
        matchesDirection && matchesPrivacy
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Call History",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Shared communication logs",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TogetherSecondaryText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Direction Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CallFilterChip(
                    label = "All",
                    isSelected = directionFilter == "ALL",
                    onClick = { viewModel.setCallDirectionFilter("ALL") }
                )
                CallFilterChip(
                    label = "Incoming",
                    isSelected = directionFilter == "INCOMING",
                    onClick = { viewModel.setCallDirectionFilter("INCOMING") }
                )
                CallFilterChip(
                    label = "Outgoing",
                    isSelected = directionFilter == "OUTGOING",
                    onClick = { viewModel.setCallDirectionFilter("OUTGOING") }
                )
                CallFilterChip(
                    label = "Missed",
                    isSelected = directionFilter == "MISSED",
                    onClick = { viewModel.setCallDirectionFilter("MISSED") }
                )
            }
        }

        if (filteredCalls.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TogetherEmptyState(
                    title = if (isSharingPaused) "Call Sharing Paused" else "No Calls Recorded",
                    description = if (isSharingPaused) {
                        "Call history sharing is currently paused in Settings."
                    } else {
                        "Calls from paired contacts will appear here automatically."
                    },
                    icon = Icons.Outlined.Call
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCalls, key = { it.id }) { call ->
                    CallHistoryCard(call = call)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CallHistoryCard(call: CallEntity) {
    val (icon, iconColor, bgPastel, directionLabel) = when (call.direction) {
        CallDirection.INCOMING -> Quadruple(Icons.Default.CallReceived, TogetherPrimaryGreen, TogetherSoftGreen, "Incoming")
        CallDirection.OUTGOING -> Quadruple(Icons.Default.CallMade, TogetherSkyBlue, TogetherSoftBlue, "Outgoing")
        CallDirection.MISSED -> Quadruple(Icons.Default.CallMissed, TogetherWarmPink, TogetherSoftPink, "Missed")
    }

    val timeFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val formattedTime = timeFormatter.format(Date(call.timestamp))

    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgPastel),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = directionLabel,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = call.contactName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TogetherPrimaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$directionLabel • ${formatDuration(call.durationSeconds)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = iconColor
                )
            }

            Text(
                text = formattedTime,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = TogetherSecondaryText
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    if (seconds <= 0) return "0s"
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
}

@Composable
private fun CallFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TogetherPrimaryText else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else TogetherSecondaryText
        )
    }
}



private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
