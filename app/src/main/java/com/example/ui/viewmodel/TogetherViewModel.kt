package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.TogetherDatabase
import com.example.data.model.AppThemeMode
import com.example.data.model.CallDirection
import com.example.data.model.CallEntity
import com.example.data.model.DrawingPoint
import com.example.data.model.DrawingStroke
import com.example.data.model.LoveLetterItem
import com.example.data.model.MessageEntity
import com.example.data.model.MilestoneItem
import com.example.data.model.MomentEntity
import com.example.data.model.PhotoMemory
import com.example.data.model.SmsContactFilter
import com.example.data.model.SongItem
import com.example.data.model.SourceType
import com.example.data.model.WhatsAppChatSummary
import com.example.data.parser.WhatsAppParser
import com.example.data.repository.TogetherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CelebrationInfo(
    val title: String,
    val subtitle: String,
    val isRomantic: Boolean = false
)

class TogetherViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TogetherDatabase.getDatabase(application)
    private val repository = TogetherRepository(database.togetherDao())

    // Active Profile: "ALEX"
    private val _activePartnerId = MutableStateFlow("ALEX")
    val activePartnerId: StateFlow<String> = _activePartnerId.asStateFlow()

    // Theme Mode: LIGHT, DARK, SYSTEM
    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    // Streak and Connection State
    private val _streakDays = MutableStateFlow(7)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // Onboarding State
    private val _isOnboardingCompleted = MutableStateFlow(true)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    // Privacy Toggles
    private val _isSharingSms = MutableStateFlow(true)
    val isSharingSms: StateFlow<Boolean> = _isSharingSms.asStateFlow()

    private val _isSharingCalls = MutableStateFlow(true)
    val isSharingCalls: StateFlow<Boolean> = _isSharingCalls.asStateFlow()

    private val _isSharingWhatsApp = MutableStateFlow(true)
    val isSharingWhatsApp: StateFlow<Boolean> = _isSharingWhatsApp.asStateFlow()

    private val _isSharingPaused = MutableStateFlow(false)
    val isSharingPaused: StateFlow<Boolean> = _isSharingPaused.asStateFlow()

    // WhatsApp Live QR Web Linking State (Starts connected in demo for immediate testing!)
    private val _isWhatsAppLinked = MutableStateFlow(true)
    val isWhatsAppLinked: StateFlow<Boolean> = _isWhatsAppLinked.asStateFlow()

    private val _linkedDeviceName = MutableStateFlow<String?>("WhatsApp Web • Chrome Companion")
    val linkedDeviceName: StateFlow<String?> = _linkedDeviceName.asStateFlow()

    // WhatsApp Chats List
    private val _whatsAppChats = MutableStateFlow<List<WhatsAppChatSummary>>(emptyList())
    val whatsAppChats: StateFlow<List<WhatsAppChatSummary>> = _whatsAppChats.asStateFlow()

    // Selected WhatsApp Chat Thread (when user clicks into a WhatsApp chat)
    private val _selectedWhatsAppChatId = MutableStateFlow<String?>(null)
    val selectedWhatsAppChatId: StateFlow<String?> = _selectedWhatsAppChatId.asStateFlow()

    // SMS Contacts Filter & Customization List
    private val _smsContacts = MutableStateFlow<List<SmsContactFilter>>(emptyList())
    val smsContacts: StateFlow<List<SmsContactFilter>> = _smsContacts.asStateFlow()

    // Filters & Search
    private val _messageSearchQuery = MutableStateFlow("")
    val messageSearchQuery: StateFlow<String> = _messageSearchQuery.asStateFlow()

    private val _messageSourceFilter = MutableStateFlow("ALL") // "ALL", "SMS", "WHATSAPP"
    val messageSourceFilter: StateFlow<String> = _messageSourceFilter.asStateFlow()

    private val _callDirectionFilter = MutableStateFlow("ALL") // "ALL", "INCOMING", "OUTGOING", "MISSED"
    val callDirectionFilter: StateFlow<String> = _callDirectionFilter.asStateFlow()

    // Celebration modal state
    private val _celebration = MutableStateFlow<CelebrationInfo?>(null)
    val celebration: StateFlow<CelebrationInfo?> = _celebration.asStateFlow()

    // Database flows
    val messages: StateFlow<List<MessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calls: StateFlow<List<CallEntity>> = repository.allCalls
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moments: StateFlow<List<MomentEntity>> = repository.allMoments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _milestones = MutableStateFlow<List<MilestoneItem>>(emptyList())
    val milestones: StateFlow<List<MilestoneItem>> = _milestones.asStateFlow()

    // Mature scrapbook state: Love Letters, Music, and Photos
    private val _loveLetters = MutableStateFlow<List<LoveLetterItem>>(emptyList())
    val loveLetters: StateFlow<List<LoveLetterItem>> = _loveLetters.asStateFlow()

    private val _sharedMusic = MutableStateFlow<List<SongItem>>(emptyList())
    val sharedMusic: StateFlow<List<SongItem>> = _sharedMusic.asStateFlow()

    private val _photoMemories = MutableStateFlow<List<PhotoMemory>>(emptyList())
    val photoMemories: StateFlow<List<PhotoMemory>> = _photoMemories.asStateFlow()

    // Currently playing song id
    private val _playingSongId = MutableStateFlow<String?>(null)
    val playingSongId: StateFlow<String?> = _playingSongId.asStateFlow()

    // Live Shared Whiteboard / Drawing Canvas State
    private val _drawingStrokes = MutableStateFlow<List<DrawingStroke>>(emptyList())
    val drawingStrokes: StateFlow<List<DrawingStroke>> = _drawingStrokes.asStateFlow()

    private val _isPartnerDrawing = MutableStateFlow(false)
    val isPartnerDrawing: StateFlow<Boolean> = _isPartnerDrawing.asStateFlow()

    init {
        viewModelScope.launch {
            repository.populateSeedDataIfEmpty()
            _milestones.value = repository.getInitialMilestones(_streakDays.value)
            _loveLetters.value = repository.getEditorialLoveLetters()
            _sharedMusic.value = repository.getSharedMusicList()
            _photoMemories.value = repository.getPhotoMemories()

            // Initialize default WhatsApp chats
            _whatsAppChats.value = listOf(
                WhatsAppChatSummary(
                    id = "wa_sam",
                    name = "Sam",
                    phoneNumber = "+1 (555) 234-5678",
                    avatarInitials = "S",
                    lastMessage = "I reserved a corner table for 7:00 at the bistro.",
                    timestamp = System.currentTimeMillis() - 1800000L,
                    unreadCount = 0,
                    isShared = true,
                    originDevice = "WhatsApp Web • Linked Companion"
                ),
                WhatsAppChatSummary(
                    id = "wa_trip",
                    name = "Weekend Cabin Trip",
                    phoneNumber = "+1 (555) 888-9999",
                    avatarInitials = "W",
                    lastMessage = "Alex: Reserving the cabin by the river right now!",
                    timestamp = System.currentTimeMillis() - 7200000L,
                    unreadCount = 2,
                    isShared = true,
                    isGroup = true,
                    originDevice = "WhatsApp Group • Synced"
                ),
                WhatsAppChatSummary(
                    id = "wa_family",
                    name = "Family Group",
                    phoneNumber = "+1 (555) 345-6789",
                    avatarInitials = "F",
                    lastMessage = "Dinner next Sunday at 6pm!",
                    timestamp = System.currentTimeMillis() - 86400000L,
                    unreadCount = 0,
                    isShared = false,
                    isGroup = true,
                    originDevice = "WhatsApp Group • Private"
                ),
                WhatsAppChatSummary(
                    id = "wa_work",
                    name = "Jordan (Work Project)",
                    phoneNumber = "+1 (555) 777-1234",
                    avatarInitials = "J",
                    lastMessage = "Quarterly deck is approved and sent.",
                    timestamp = System.currentTimeMillis() - 43200000L,
                    unreadCount = 0,
                    isShared = false,
                    originDevice = "Direct Chat • Private"
                )
            )

            // Initialize default SMS Contacts Filter
            _smsContacts.value = listOf(
                SmsContactFilter(
                    phoneNumber = "+1 (555) 234-5678",
                    contactName = "Sam",
                    isShared = true,
                    messageCount = 12
                ),
                SmsContactFilter(
                    phoneNumber = "+1 (555) 987-6543",
                    contactName = "Alex",
                    isShared = true,
                    messageCount = 14
                ),
                SmsContactFilter(
                    phoneNumber = "+1 (555) 444-5555",
                    contactName = "Old Town Bistro",
                    isShared = true,
                    messageCount = 3
                ),
                SmsContactFilter(
                    phoneNumber = "+1 (555) 999-0000",
                    contactName = "Bank 2FA (Automated)",
                    isShared = false,
                    messageCount = 5
                )
            )

            // Seed initial collaborative heart sketch on the live canvas
            initDefaultCanvasStrokes()
        }
    }

    private fun initDefaultCanvasStrokes() {
        val heartPoints = listOf(
            DrawingPoint(150f, 130f),
            DrawingPoint(130f, 100f),
            DrawingPoint(100f, 100f),
            DrawingPoint(80f, 120f),
            DrawingPoint(80f, 150f),
            DrawingPoint(150f, 220f),
            DrawingPoint(220f, 150f),
            DrawingPoint(220f, 120f),
            DrawingPoint(200f, 100f),
            DrawingPoint(170f, 100f),
            DrawingPoint(150f, 130f)
        )
        _drawingStrokes.value = listOf(
            DrawingStroke(
                id = "stroke_init_1",
                points = heartPoints,
                colorHex = "#F26D85",
                strokeWidth = 6f,
                author = "Sam"
            )
        )
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun selectWhatsAppChat(chatId: String?) {
        _selectedWhatsAppChatId.value = chatId
    }

    fun addDrawingStroke(stroke: DrawingStroke) {
        _drawingStrokes.value = _drawingStrokes.value + stroke
    }

    fun undoDrawingStroke() {
        if (_drawingStrokes.value.isNotEmpty()) {
            _drawingStrokes.value = _drawingStrokes.value.dropLast(1)
        }
    }

    fun clearDrawingCanvas() {
        _drawingStrokes.value = emptyList()
    }

    fun simulatePartnerDoodle() {
        viewModelScope.launch {
            _isPartnerDrawing.value = true
            val now = System.currentTimeMillis()
            // Add a star or sweet doodle from Sam
            val starPoints = listOf(
                DrawingPoint(240f, 80f),
                DrawingPoint(250f, 110f),
                DrawingPoint(280f, 110f),
                DrawingPoint(255f, 130f),
                DrawingPoint(265f, 160f),
                DrawingPoint(240f, 140f),
                DrawingPoint(215f, 160f),
                DrawingPoint(225f, 130f),
                DrawingPoint(200f, 110f),
                DrawingPoint(230f, 110f),
                DrawingPoint(240f, 80f)
            )
            val newStroke = DrawingStroke(
                id = "stroke_partner_$now",
                points = starPoints,
                colorHex = "#58CC02",
                strokeWidth = 5f,
                author = "Sam"
            )
            _drawingStrokes.value = _drawingStrokes.value + newStroke
            _isPartnerDrawing.value = false
            triggerCelebration(
                title = "Sam Added a Drawing",
                subtitle = "New doodle drawn on your shared canvas.",
                isRomantic = true
            )
        }
    }

    fun saveDrawingToMemories() {
        viewModelScope.launch {
            val count = _drawingStrokes.value.size
            if (count > 0) {
                addMoment(
                    title = "Shared Whiteboard Sketch",
                    description = "A spontaneous collaborative drawing drawn together on our live canvas.",
                    category = "Drawing",
                    extraMeta = "Canvas with $count strokes"
                )
            }
        }
    }

    fun linkWhatsApp(deviceName: String = "WhatsApp Web • Chrome Companion") {
        _isWhatsAppLinked.value = true
        _linkedDeviceName.value = deviceName

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val hour = 3600000L
            val linkedMessages = listOf(
                MessageEntity(
                    senderName = "Sam",
                    senderId = "SAM",
                    recipientName = "Alex",
                    text = "Connected via WhatsApp Web! Both our phone and laptop chats are live in sync now.",
                    timestamp = now - (hour / 2),
                    sourceType = SourceType.WHATSAPP,
                    isShared = true,
                    contactNumber = "+1 (555) 234-5678",
                    originDetails = "WhatsApp Web • Active Session"
                ),
                MessageEntity(
                    senderName = "Alex",
                    senderId = "ALEX",
                    recipientName = "Sam",
                    text = "Awesome! I can see the conversation instantly.",
                    timestamp = now - (hour / 3),
                    sourceType = SourceType.WHATSAPP,
                    isShared = true,
                    contactNumber = "+1 (555) 987-6543",
                    originDetails = "WhatsApp Web • Linked Companion"
                )
            )
            repository.insertMessages(linkedMessages)
        }

        triggerCelebration(
            title = "WhatsApp Linked Successfully",
            subtitle = "Your WhatsApp conversations are now live and connected in TogetherSync."
        )
    }

    fun unlinkWhatsApp() {
        _isWhatsAppLinked.value = false
        triggerCelebration(
            title = "Device Unlinked",
            subtitle = "WhatsApp Web companion session has been disconnected."
        )
    }

    fun toggleWhatsAppChatSharing(chatId: String) {
        _whatsAppChats.value = _whatsAppChats.value.map { chat ->
            if (chat.id == chatId) chat.copy(isShared = !chat.isShared) else chat
        }
    }

    fun toggleSmsContactSharing(phoneNumber: String) {
        _smsContacts.value = _smsContacts.value.map { contact ->
            if (contact.phoneNumber == phoneNumber) contact.copy(isShared = !contact.isShared) else contact
        }
    }

    fun updateSmsContact(originalNumber: String, newName: String, newNumber: String) {
        _smsContacts.value = _smsContacts.value.map { contact ->
            if (contact.phoneNumber == originalNumber) {
                contact.copy(contactName = newName.trim(), phoneNumber = newNumber.trim())
            } else {
                contact
            }
        }
        triggerCelebration(
            title = "Contact Updated",
            subtitle = "Saved $newName ($newNumber) in shared filter preferences."
        )
    }

    fun completeOnboarding() {
        _isOnboardingCompleted.value = true
    }

    fun restartOnboarding() {
        _isOnboardingCompleted.value = false
    }

    fun setMessageSearchQuery(query: String) {
        _messageSearchQuery.value = query
    }

    fun setMessageSourceFilter(filter: String) {
        _messageSourceFilter.value = filter
    }

    fun switchPartner() {
        _activePartnerId.value = if (_activePartnerId.value == "ALEX") "SAM" else "ALEX"
    }

    fun setCallDirectionFilter(filter: String) {
        _callDirectionFilter.value = filter
    }

    fun toggleSongPlay(songId: String) {
        _playingSongId.value = if (_playingSongId.value == songId) null else songId
    }

    fun toggleShareSms() {
        _isSharingSms.value = !_isSharingSms.value
    }

    fun toggleShareCalls() {
        _isSharingCalls.value = !_isSharingCalls.value
    }

    fun toggleShareWhatsApp() {
        _isSharingWhatsApp.value = !_isSharingWhatsApp.value
    }

    fun togglePauseSharing() {
        _isSharingPaused.value = !_isSharingPaused.value
    }

    fun sendMessage(text: String, sourceType: SourceType, recipientContact: String = "Sam", contactNumber: String = "+1 (555) 234-5678") {
        if (text.isBlank()) return
        viewModelScope.launch {
            val isAlex = _activePartnerId.value == "ALEX"
            val sender = if (isAlex) "Alex" else "Sam"
            val origin = if (sourceType == SourceType.WHATSAPP) "WhatsApp Web • Linked Companion" else "Carrier SMS • Encrypted"

            val message = MessageEntity(
                senderName = sender,
                senderId = _activePartnerId.value,
                recipientName = recipientContact,
                text = text.trim(),
                sourceType = sourceType,
                isShared = true,
                contactNumber = contactNumber,
                originDetails = origin
            )
            repository.insertMessage(message)
        }
    }

    fun importWhatsAppChat(rawText: String) {
        viewModelScope.launch {
            val isAlex = _activePartnerId.value == "ALEX"
            val defaultSender = if (isAlex) "Alex" else "Sam"
            val recipient = if (isAlex) "Sam" else "Alex"

            val parsedMessages = WhatsAppParser.parseChatExport(
                rawText = rawText,
                defaultSender = defaultSender,
                recipient = recipient
            )

            if (parsedMessages.isNotEmpty()) {
                repository.insertMessages(parsedMessages)
                triggerCelebration(
                    title = "Conversation Imported",
                    subtitle = "Successfully added ${parsedMessages.size} messages to your shared conversation space."
                )
            }
        }
    }

    fun addMoment(title: String, description: String, category: String, extraMeta: String = "") {
        viewModelScope.launch {
            val author = if (_activePartnerId.value == "ALEX") "Alex" else "Sam"
            val moment = MomentEntity(
                title = title.trim(),
                description = description.trim(),
                category = category,
                authorPartner = author,
                extraMeta = extraMeta.trim()
            )
            repository.insertMoment(moment)
            triggerCelebration(
                title = "Added to Our World",
                subtitle = "\"$title\" is now part of your shared archive."
            )
        }
    }

    fun addLoveLetter(subject: String, body: String) {
        val author = if (_activePartnerId.value == "ALEX") "Alex" else "Sam"
        val newLetter = LoveLetterItem(
            id = "letter_${System.currentTimeMillis()}",
            sender = author,
            date = "Today",
            subject = subject.trim(),
            body = body.trim()
        )
        _loveLetters.value = listOf(newLetter) + _loveLetters.value
        triggerCelebration(
            title = "Letter Sent",
            subtitle = "Your love letter is waiting for ${if (author == "Alex") "Sam" else "Alex"} in Our World.",
            isRomantic = true
        )
    }

    fun addSong(title: String, artist: String, dedication: String) {
        val newSong = SongItem(
            id = "song_${System.currentTimeMillis()}",
            title = title.trim(),
            artist = artist.trim(),
            dedication = dedication.trim(),
            duration = "3:30"
        )
        _sharedMusic.value = listOf(newSong) + _sharedMusic.value
        triggerCelebration(
            title = "Song Added",
            subtitle = "\"$title\" was added to your shared soundtrack."
        )
    }

    fun deleteMessage(id: Long) {
        viewModelScope.launch {
            repository.deleteMessage(id)
        }
    }

    fun deleteCall(id: Long) {
        viewModelScope.launch {
            repository.deleteCall(id)
        }
    }

    fun deleteMoment(id: Long) {
        viewModelScope.launch {
            repository.deleteMoment(id)
        }
    }

    fun revokeMyData() {
        viewModelScope.launch {
            repository.clearAllMessages()
            repository.clearAllCalls()
            triggerCelebration(
                title = "Data Revoked",
                subtitle = "All shared conversations and call history have been cleared."
            )
        }
    }

    fun triggerCelebration(title: String, subtitle: String, isRomantic: Boolean = false) {
        _celebration.value = CelebrationInfo(title, subtitle, isRomantic)
    }

    fun dismissCelebration() {
        _celebration.value = null
    }
}
