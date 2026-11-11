package com.example.data.repository

import com.example.data.db.TogetherDao
import com.example.data.model.CallDirection
import com.example.data.model.CallEntity
import com.example.data.model.LoveLetterItem
import com.example.data.model.MessageEntity
import com.example.data.model.MilestoneItem
import com.example.data.model.MomentEntity
import com.example.data.model.PhotoMemory
import com.example.data.model.SongItem
import com.example.data.model.SourceType
import kotlinx.coroutines.flow.Flow

class TogetherRepository(private val dao: TogetherDao) {

    val allMessages: Flow<List<MessageEntity>> = dao.getAllMessages()
    val allCalls: Flow<List<CallEntity>> = dao.getAllCalls()
    val allMoments: Flow<List<MomentEntity>> = dao.getAllMoments()

    suspend fun insertMessage(message: MessageEntity) = dao.insertMessage(message)
    suspend fun insertMessages(messages: List<MessageEntity>) = dao.insertMessages(messages)
    suspend fun deleteMessage(id: Long) = dao.deleteMessage(id)
    suspend fun clearMessagesBySource(sourceType: SourceType) = dao.clearMessagesBySource(sourceType)
    suspend fun clearAllMessages() = dao.clearAllMessages()

    suspend fun insertCall(call: CallEntity) = dao.insertCall(call)
    suspend fun insertCalls(calls: List<CallEntity>) = dao.insertCalls(calls)
    suspend fun deleteCall(id: Long) = dao.deleteCall(id)
    suspend fun clearAllCalls() = dao.clearAllCalls()

    suspend fun insertMoment(moment: MomentEntity) = dao.insertMoment(moment)
    suspend fun deleteMoment(id: Long) = dao.deleteMoment(id)
    suspend fun clearAllMoments() = dao.clearAllMoments()

    fun getInitialMilestones(streakDays: Int): List<MilestoneItem> {
        return listOf(
            MilestoneItem(
                id = "m1",
                title = "Connected Space",
                description = "Began our private shared sanctuary together.",
                daysRequired = 1,
                isUnlocked = true,
                iconName = "favorite",
                colorHex = "#58CC02"
            ),
            MilestoneItem(
                id = "m2",
                title = "One Week Milestone",
                description = "Seven consecutive days of voluntary connection.",
                daysRequired = 7,
                isUnlocked = streakDays >= 7,
                iconName = "timeline",
                colorHex = "#F26D85"
            ),
            MilestoneItem(
                id = "m3",
                title = "First Shared Letter",
                description = "Exchanged our first written note in Our World.",
                daysRequired = 3,
                isUnlocked = true,
                iconName = "mail",
                colorHex = "#9274D3"
            ),
            MilestoneItem(
                id = "m4",
                title = "First Shared Archive",
                description = "Imported an exported conversation to keep forever.",
                daysRequired = 5,
                isUnlocked = true,
                iconName = "chat",
                colorHex = "#2B8CE6"
            ),
            MilestoneItem(
                id = "m5",
                title = "One Month Devotion",
                description = "Thirty days of mindful, transparent companionship.",
                daysRequired = 30,
                isUnlocked = streakDays >= 30,
                iconName = "event",
                colorHex = "#F5A623"
            )
        )
    }

    fun getEditorialLoveLetters(): List<LoveLetterItem> {
        return listOf(
            LoveLetterItem(
                id = "l1",
                sender = "Alex",
                date = "November 8",
                subject = "The quiet moments with you",
                body = "Even when work gets overwhelmingly hectic, knowing I get to walk home to you makes the whole day feel lighter. Thank you for the tea this morning before I woke up. You notice all the little things, and I love you for every single one."
            ),
            LoveLetterItem(
                id = "l2",
                sender = "Sam",
                date = "November 4",
                subject = "Remembering our walk by the pier",
                body = "Watching the sunset while the chilly wind blew through the harbor was one of those moments where time just paused. Having you hold my coat closer around me felt warmer than any fireplace. Looking forward to our getaway weekend."
            ),
            LoveLetterItem(
                id = "l3",
                sender = "Alex",
                date = "October 29",
                subject = "Always in your corner",
                body = "Just a quick reminder before your big client review today: nobody works harder or cares deeper than you do. Whatever happens today, you are already brilliant. I'm cheering for you always."
            )
        )
    }

    fun getSharedMusicList(): List<SongItem> {
        return listOf(
            SongItem(
                id = "s1",
                title = "Until I Found You",
                artist = "Stephen Sanchez",
                dedication = "Played on the radio during our first road trip down Highway 1.",
                duration = "2:57"
            ),
            SongItem(
                id = "s2",
                title = "Golden Hour",
                artist = "JVKE",
                dedication = "Our favorite song to make pasta to on Sunday evenings.",
                duration = "3:29"
            ),
            SongItem(
                id = "s3",
                title = "Lover",
                artist = "Taylor Swift",
                dedication = "The song we danced to in the living room at midnight.",
                duration = "3:41"
            )
        )
    }

    fun getPhotoMemories(): List<PhotoMemory> {
        return listOf(
            PhotoMemory(
                id = "p1",
                title = "Stargazing at Pine Hill",
                location = "Cascade National Park",
                date = "Last Saturday",
                placeholderColor = 0xFF2D3748
            ),
            PhotoMemory(
                id = "p2",
                title = "First Coffee of Autumn",
                location = "Old Town Bakery",
                date = "October 18",
                placeholderColor = 0xFF744210
            ),
            PhotoMemory(
                id = "p3",
                title = "Sunday Afternoon Bike Ride",
                location = "Harbor Trail",
                date = "October 5",
                placeholderColor = 0xFF22543D
            )
        )
    }

    suspend fun populateSeedDataIfEmpty() {
        val now = System.currentTimeMillis()
        val hour = 3600000L

        val initialMessages = listOf(
            MessageEntity(
                senderName = "Alex",
                senderId = "ALEX",
                recipientName = "Sam",
                text = "Good morning! Just got to the office. Don't forget your umbrella today, it looks like rain.",
                timestamp = now - (hour * 5),
                sourceType = SourceType.SMS,
                isShared = true
            ),
            MessageEntity(
                senderName = "Sam",
                senderId = "SAM",
                recipientName = "Alex",
                text = "Morning! Picked it up just in time. Hope your morning presentation goes smoothly.",
                timestamp = now - (hour * 4),
                sourceType = SourceType.SMS,
                isShared = true
            ),
            MessageEntity(
                senderName = "Alex",
                senderId = "ALEX",
                recipientName = "Sam",
                text = "The review went wonderfully! Shall we have dinner at our favorite bistro tonight?",
                timestamp = now - (hour * 2),
                sourceType = SourceType.WHATSAPP,
                isShared = true
            ),
            MessageEntity(
                senderName = "Sam",
                senderId = "SAM",
                recipientName = "Alex",
                text = "You earned every bit of it. I reserved a corner table for 7:00.",
                timestamp = now - (hour * 1),
                sourceType = SourceType.WHATSAPP,
                isShared = true
            )
        )
        dao.insertMessages(initialMessages)

        val initialCalls = listOf(
            CallEntity(
                contactName = "Sam",
                phoneNumber = "+1 (555) 234-5678",
                timestamp = now - (hour * 3),
                durationSeconds = 480, // 8 mins
                direction = CallDirection.INCOMING,
                isShared = true
            ),
            CallEntity(
                contactName = "Alex",
                phoneNumber = "+1 (555) 987-6543",
                timestamp = now - (hour * 6),
                durationSeconds = 120, // 2 mins
                direction = CallDirection.OUTGOING,
                isShared = true
            ),
            CallEntity(
                contactName = "Sam",
                phoneNumber = "+1 (555) 234-5678",
                timestamp = now - (hour * 26),
                durationSeconds = 0,
                direction = CallDirection.MISSED,
                isShared = true
            ),
            CallEntity(
                contactName = "Alex",
                phoneNumber = "+1 (555) 987-6543",
                timestamp = now - (hour * 32),
                durationSeconds = 1420, // 23 mins
                direction = CallDirection.INCOMING,
                isShared = true
            )
        )
        dao.insertCalls(initialCalls)

        val initialMoments = listOf(
            MomentEntity(
                title = "Stargazing at Pine Hill",
                description = "Brought a thermos of hot cocoa and counted four shooting stars under a crisp autumn sky.",
                category = "Memory",
                timestamp = now - (hour * 48),
                authorPartner = "Alex",
                extraMeta = "Cascade Overlook"
            ),
            MomentEntity(
                title = "Our Shared Space Began",
                description = "Joined TogetherSync to stay quietly close no matter how busy the days get.",
                category = "Memory",
                timestamp = now - (hour * 168),
                authorPartner = "Sam",
                extraMeta = "Anniversary Day"
            ),
            MomentEntity(
                title = "Cabin Getaway Weekend",
                description = "Booked the wooden cabin with the stone fireplace for the end of the month.",
                category = "Note",
                timestamp = now - (hour * 96),
                authorPartner = "Alex",
                extraMeta = "November 24-26"
            )
        )
        for (m in initialMoments) {
            dao.insertMoment(m)
        }
    }
}
