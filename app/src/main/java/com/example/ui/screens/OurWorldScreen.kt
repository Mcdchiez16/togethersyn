package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Photo
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.LoveLetterItem
import com.example.data.model.MomentEntity
import com.example.data.model.PhotoMemory
import com.example.data.model.SongItem
import com.example.ui.components.TogetherButton
import com.example.ui.components.TogetherButtonVariant
import com.example.ui.components.TogetherCard
import com.example.ui.components.TogetherDrawingCanvas
import com.example.ui.components.TogetherMilestoneCard
import com.example.ui.theme.TogetherBorder
import com.example.ui.theme.TogetherCardBg
import com.example.ui.theme.TogetherPrimaryGreen
import com.example.ui.theme.TogetherPrimaryText
import com.example.ui.theme.TogetherSecondaryText
import com.example.ui.theme.TogetherSoftGreen
import com.example.ui.theme.TogetherSoftPink
import com.example.ui.theme.TogetherSoftPurple
import com.example.ui.theme.TogetherWarmPink
import com.example.ui.viewmodel.TogetherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OurWorldScreen(
    viewModel: TogetherViewModel,
    modifier: Modifier = Modifier
) {
    val loveLetters by viewModel.loveLetters.collectAsState()
    val sharedMusic by viewModel.sharedMusic.collectAsState()
    val photoMemories by viewModel.photoMemories.collectAsState()
    val moments by viewModel.moments.collectAsState()
    val milestones by viewModel.milestones.collectAsState()
    val playingSongId by viewModel.playingSongId.collectAsState()
    val activePartnerId by viewModel.activePartnerId.collectAsState()
    val drawingStrokes by viewModel.drawingStrokes.collectAsState()
    val isPartnerDrawing by viewModel.isPartnerDrawing.collectAsState()

    var selectedSection by remember { mutableStateOf("ALL") } // "ALL", "DRAW", "LETTERS", "MUSIC", "PHOTOS"
    var showWriteLetterDialog by remember { mutableStateOf(false) }
    var showAddSongDialog by remember { mutableStateOf(false) }
    var showAddMemoryDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Our World",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Shared canvas, music, notes, and photos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TogetherSecondaryText
                    )
                }

                TogetherButton(
                    text = "New Letter",
                    onClick = { showWriteLetterDialog = true },
                    variant = TogetherButtonVariant.WARM_PINK,
                    icon = Icons.Outlined.MailOutline,
                    height = 38.dp
                )
            }
        }

        // Editorial Section Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SectionPill(
                        label = "Overview",
                        isSelected = selectedSection == "ALL",
                        onClick = { selectedSection = "ALL" }
                    )
                }
                item {
                    SectionPill(
                        label = "Draw Together",
                        isSelected = selectedSection == "DRAW",
                        onClick = { selectedSection = "DRAW" }
                    )
                }
                item {
                    SectionPill(
                        label = "Love Letters",
                        isSelected = selectedSection == "LETTERS",
                        onClick = { selectedSection = "LETTERS" }
                    )
                }
                item {
                    SectionPill(
                        label = "Our Music",
                        isSelected = selectedSection == "MUSIC",
                        onClick = { selectedSection = "MUSIC" }
                    )
                }
                item {
                    SectionPill(
                        label = "Photos",
                        isSelected = selectedSection == "PHOTOS",
                        onClick = { selectedSection = "PHOTOS" }
                    )
                }
            }
        }

        // 1. LIVE COLLABORATIVE DRAWING CANVAS (WHITE SPACE)
        if (selectedSection == "ALL" || selectedSection == "DRAW") {
            item {
                SectionHeader(
                    title = "Live Shared Whiteboard",
                    actionText = if (selectedSection != "DRAW") "Expand Canvas" else null,
                    onAction = if (selectedSection != "DRAW") { { selectedSection = "DRAW" } } else null
                )
            }

            item {
                TogetherDrawingCanvas(
                    strokes = drawingStrokes,
                    isPartnerDrawing = isPartnerDrawing,
                    onAddStroke = { viewModel.addDrawingStroke(it) },
                    onUndo = { viewModel.undoDrawingStroke() },
                    onClear = { viewModel.clearDrawingCanvas() },
                    onSimulatePartnerDoodle = { viewModel.simulatePartnerDoodle() },
                    onSaveToMemories = { viewModel.saveDrawingToMemories() },
                    authorName = if (activePartnerId == "ALEX") "Alex" else "Sam"
                )
            }
        }

        // 1. LOVE LETTERS SECTION
        if (selectedSection == "ALL" || selectedSection == "LETTERS") {
            item {
                SectionHeader(
                    title = "Love Letters",
                    actionText = "Write Letter",
                    onAction = { showWriteLetterDialog = true }
                )
            }

            items(loveLetters, key = { it.id }) { letter ->
                LoveLetterCard(letter = letter)
            }
        }

        // 2. OUR MUSIC SECTION
        if (selectedSection == "ALL" || selectedSection == "MUSIC") {
            item {
                SectionHeader(
                    title = "Our Music",
                    actionText = "Add Song",
                    onAction = { showAddSongDialog = true }
                )
            }

            items(sharedMusic, key = { it.id }) { song ->
                SongPreviewCard(
                    song = song,
                    isPlaying = playingSongId == song.id,
                    onTogglePlay = { viewModel.toggleSongPlay(song.id) }
                )
            }
        }

        // 3. OUR PHOTOS SECTION
        if (selectedSection == "ALL" || selectedSection == "PHOTOS") {
            item {
                SectionHeader(
                    title = "Our Photos",
                    actionText = null,
                    onAction = null
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(photoMemories, key = { it.id }) { photo ->
                        PhotoMemoryCard(photo = photo)
                    }
                }
            }
        }

        // 4. MEMORIES & MILESTONES
        if (selectedSection == "ALL") {
            item {
                SectionHeader(
                    title = "Milestones & Timeline",
                    actionText = "Add Memory",
                    onAction = { showAddMemoryDialog = true }
                )
            }

            items(milestones, key = { it.id }) { milestone ->
                TogetherMilestoneCard(
                    milestone = milestone,
                    onClick = {
                        if (milestone.isUnlocked) {
                            viewModel.triggerCelebration(
                                title = milestone.title,
                                subtitle = milestone.description
                            )
                        }
                    }
                )
            }

            items(moments, key = { it.id }) { moment ->
                EditorialMemoryCard(moment = moment)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Write Love Letter Dialog
    if (showWriteLetterDialog) {
        WriteLoveLetterDialog(
            senderName = if (activePartnerId == "ALEX") "Alex" else "Sam",
            onDismiss = { showWriteLetterDialog = false },
            onSend = { subject, body ->
                viewModel.addLoveLetter(subject, body)
                showWriteLetterDialog = false
            }
        )
    }

    // Add Song Dialog
    if (showAddSongDialog) {
        AddSongDialog(
            onDismiss = { showAddSongDialog = false },
            onAdd = { title, artist, dedication ->
                viewModel.addSong(title, artist, dedication)
                showAddSongDialog = false
            }
        )
    }

    // Add Memory Dialog
    if (showAddMemoryDialog) {
        AddMemoryEditorialDialog(
            onDismiss = { showAddMemoryDialog = false },
            onAdd = { title, desc, category, meta ->
                viewModel.addMoment(title, desc, category, meta)
                showAddMemoryDialog = false
            }
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String?,
    onAction: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TogetherPrimaryGreen,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun SectionPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) TogetherPrimaryText else MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                if (isSelected) TogetherPrimaryText else TogetherBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else TogetherSecondaryText
        )
    }
}

@Composable
fun LoveLetterCard(letter: LoveLetterItem) {
    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(TogetherSoftPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = TogetherWarmPink,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "From ${letter.sender}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TogetherPrimaryText
                    )
                }

                Text(
                    text = letter.date,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = TogetherSecondaryText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = letter.subject,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TogetherPrimaryText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = letter.body,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A48)
            )
        }
    }
}

@Composable
fun SongPreviewCard(
    song: SongItem,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit
) {
    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art style tile
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF232528)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TogetherPrimaryText
                )
                Text(
                    text = song.artist,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TogetherSecondaryText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = song.dedication,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6E6E6A)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isPlaying) TogetherPrimaryGreen else TogetherSoftGreen)
                    .clickable(onClick = onTogglePlay),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = if (isPlaying) Color.White else TogetherPrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PhotoMemoryCard(photo: PhotoMemory) {
    TogetherCard(
        modifier = Modifier.width(180.dp),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 14.dp
    ) {
        Column {
            // Editorial Photo Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(Color(photo.placeholderColor)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Photo,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = photo.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TogetherPrimaryText,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TogetherSecondaryText,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = photo.location,
                        fontSize = 11.sp,
                        color = TogetherSecondaryText,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun EditorialMemoryCard(moment: MomentEntity) {
    val timeFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val formattedDate = timeFormatter.format(Date(moment.timestamp))

    TogetherCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = TogetherCardBg,
        borderColor = TogetherBorder,
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = moment.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TogetherPrimaryText
                )
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = TogetherSecondaryText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = moment.description,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xFF4A4A48)
            )

            if (moment.extraMeta.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TogetherSoftPurple)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = moment.extraMeta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7554C0)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Noted by ${moment.authorPartner}",
                        fontSize = 11.sp,
                        color = TogetherSecondaryText
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteLoveLetterDialog(
    senderName: String,
    onDismiss: () -> Unit,
    onSend: (String, String) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = Color(0xFFFFD4DE),
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Write a Love Letter",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TogetherPrimaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A private note for your partner to cherish.",
                    fontSize = 13.sp,
                    color = TogetherSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = { Text("Subject or title...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherWarmPink,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    placeholder = { Text("Write your thoughts...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherWarmPink,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                TogetherButton(
                    text = "Send Letter",
                    onClick = {
                        if (subject.isNotBlank() && body.isNotBlank()) {
                            onSend(subject, body)
                        }
                    },
                    variant = TogetherButtonVariant.WARM_PINK,
                    enabled = subject.isNotBlank() && body.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TogetherButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 40.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var dedication by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add to Our Music",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TogetherPrimaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Song title") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    placeholder = { Text("Artist") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dedication,
                    onValueChange = { dedication = it },
                    placeholder = { Text("Personal note or memory...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                TogetherButton(
                    text = "Add Song",
                    onClick = {
                        if (title.isNotBlank() && artist.isNotBlank()) {
                            onAdd(title, artist, dedication)
                        }
                    },
                    variant = TogetherButtonVariant.PRIMARY_GREEN,
                    enabled = title.isNotBlank() && artist.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TogetherButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 40.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryEditorialDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var locationOrDate by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        TogetherCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TogetherCardBg,
            borderColor = TogetherBorder,
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add a Shared Memory",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TogetherPrimaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Memory title") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("What made this special...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = locationOrDate,
                    onValueChange = { locationOrDate = it },
                    placeholder = { Text("Location or date (optional)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TogetherPrimaryGreen,
                        unfocusedBorderColor = TogetherBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                TogetherButton(
                    text = "Save Memory",
                    onClick = {
                        if (title.isNotBlank()) {
                            onAdd(title, description, "Memory", locationOrDate)
                        }
                    },
                    variant = TogetherButtonVariant.PRIMARY_GREEN,
                    enabled = title.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TogetherButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    variant = TogetherButtonVariant.OUTLINED_NEUTRAL,
                    height = 40.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
