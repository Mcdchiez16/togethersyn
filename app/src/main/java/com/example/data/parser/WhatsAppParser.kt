package com.example.data.parser

import com.example.data.model.MessageEntity
import com.example.data.model.SourceType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

object WhatsAppParser {

    // Common WhatsApp export patterns:
    // Pattern 1: "11/10/26, 14:32 - Alex: Message" or "11/10/2026, 2:32 PM - Alex: Message"
    // Pattern 2: "[11/10/26, 14:32:10] Alex: Message"
    private val linePattern1 = Pattern.compile(
        "^(\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}(?::\\d{2})?(?:\\s+[APMapm]{2})?)\\s+-\\s+([^:]+):\\s+(.+)$"
    )

    private val linePattern2 = Pattern.compile(
        "^\\[(\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}(?::\\d{2})?(?:\\s+[APMapm]{2})?)\\]\\s+([^:]+):\\s+(.+)$"
    )

    fun parseChatExport(
        rawText: String,
        defaultSender: String = "Sam",
        recipient: String = "Alex"
    ): List<MessageEntity> {
        val result = mutableListOf<MessageEntity>()
        val lines = rawText.lines()

        var currentSender = defaultSender
        var currentText = StringBuilder()
        var currentTimestamp = System.currentTimeMillis()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            val matcher1 = linePattern1.matcher(trimmed)
            val matcher2 = linePattern2.matcher(trimmed)

            if (matcher1.matches()) {
                if (currentText.isNotEmpty()) {
                    result.add(
                        MessageEntity(
                            senderName = currentSender,
                            senderId = if (currentSender.contains("Alex", ignoreCase = true)) "ALEX" else "SAM",
                            recipientName = recipient,
                            text = currentText.toString().trim(),
                            timestamp = currentTimestamp,
                            sourceType = SourceType.WHATSAPP,
                            isShared = true
                        )
                    )
                    currentText = StringBuilder()
                }

                val dateStr = matcher1.group(1) ?: ""
                currentSender = matcher1.group(2)?.trim() ?: defaultSender
                currentText.append(matcher1.group(3) ?: "")
                currentTimestamp = parseTimestamp(dateStr)
            } else if (matcher2.matches()) {
                if (currentText.isNotEmpty()) {
                    result.add(
                        MessageEntity(
                            senderName = currentSender,
                            senderId = if (currentSender.contains("Alex", ignoreCase = true)) "ALEX" else "SAM",
                            recipientName = recipient,
                            text = currentText.toString().trim(),
                            timestamp = currentTimestamp,
                            sourceType = SourceType.WHATSAPP,
                            isShared = true
                        )
                    )
                    currentText = StringBuilder()
                }

                val dateStr = matcher2.group(1) ?: ""
                currentSender = matcher2.group(2)?.trim() ?: defaultSender
                currentText.append(matcher2.group(3) ?: "")
                currentTimestamp = parseTimestamp(dateStr)
            } else {
                // Continuation of a multi-line message
                if (currentText.isNotEmpty()) {
                    currentText.append("\n").append(trimmed)
                } else {
                    // Raw message line without standard header
                    currentText.append(trimmed)
                }
            }
        }

        if (currentText.isNotEmpty()) {
            result.add(
                MessageEntity(
                    senderName = currentSender,
                    senderId = if (currentSender.contains("Alex", ignoreCase = true)) "ALEX" else "SAM",
                    recipientName = recipient,
                    text = currentText.toString().trim(),
                    timestamp = currentTimestamp,
                    sourceType = SourceType.WHATSAPP,
                    isShared = true
                )
            )
        }

        return result
    }

    private fun parseTimestamp(dateStr: String): Long {
        val formats = listOf(
            "MM/dd/yy, HH:mm:ss",
            "MM/dd/yy, HH:mm",
            "dd/MM/yy, HH:mm:ss",
            "dd/MM/yy, HH:mm",
            "MM/dd/yyyy, h:mm a",
            "dd/MM/yyyy, h:mm a"
        )
        for (pattern in formats) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                val date = sdf.parse(dateStr)
                if (date != null) return date.time
            } catch (_: Exception) {
            }
        }
        return System.currentTimeMillis()
    }
}
