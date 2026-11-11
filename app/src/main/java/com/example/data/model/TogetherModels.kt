package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SourceType {
    SMS,
    WHATSAPP
}

enum class CallDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderName: String,
    val senderId: String, // "ALEX" or "SAM"
    val recipientName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val sourceType: SourceType = SourceType.SMS,
    val isShared: Boolean = true,
    val contactNumber: String = "+1 (555) 234-5678",
    val originDetails: String = "WhatsApp Web • Linked Companion"
)

@Entity(tableName = "calls")
data class CallEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contactName: String,
    val phoneNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int = 0,
    val direction: CallDirection = CallDirection.INCOMING,
    val isShared: Boolean = true
)

@Entity(tableName = "moments")
data class MomentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "Memory", "Letter", "Note", "Music", "Photo", "Drawing"
    val timestamp: Long = System.currentTimeMillis(),
    val authorPartner: String = "Alex",
    val extraMeta: String = "" // Location for photo, Artist for music, or subtitle
)

data class LoveLetterItem(
    val id: String,
    val sender: String,
    val date: String,
    val subject: String,
    val body: String
)

data class SongItem(
    val id: String,
    val title: String,
    val artist: String,
    val dedication: String,
    val duration: String
)

data class PhotoMemory(
    val id: String,
    val title: String,
    val location: String,
    val date: String,
    val placeholderColor: Long
)

data class MilestoneItem(
    val id: String,
    val title: String,
    val description: String,
    val daysRequired: Int,
    val isUnlocked: Boolean,
    val iconName: String,
    val colorHex: String
)

data class PartnerProfile(
    val id: String, // "ALEX" or "SAM"
    val displayName: String,
    val partnerName: String,
    val avatarInitials: String,
    val statusMessage: String,
    val streakDays: Int,
    val isConnected: Boolean,
    val isSharingSms: Boolean = true,
    val isSharingCalls: Boolean = true,
    val isSharingWhatsApp: Boolean = true,
    val isSharingPaused: Boolean = false
)

data class WhatsAppChatSummary(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarInitials: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0,
    val isShared: Boolean = true,
    val isGroup: Boolean = false,
    val originDevice: String = "WhatsApp Web • Active Session"
)

data class SmsContactFilter(
    val phoneNumber: String,
    val contactName: String,
    val isShared: Boolean = true,
    val messageCount: Int = 0
)

data class DrawingPoint(
    val x: Float,
    val y: Float
)

data class DrawingStroke(
    val id: String,
    val points: List<DrawingPoint>,
    val colorHex: String,
    val strokeWidth: Float,
    val author: String = "You",
    val timestamp: Long = System.currentTimeMillis()
)
