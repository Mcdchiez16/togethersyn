package com.example

import com.example.data.parser.WhatsAppParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testWhatsAppChatParsing() {
        val sample = """
            11/10/26, 14:32 - Alex: Hello from Alex!
            11/10/26, 14:35 - Sam: Hey Alex! So glad to sync up 💚
        """.trimIndent()

        val parsed = WhatsAppParser.parseChatExport(sample)
        assertEquals(2, parsed.size)
        assertEquals("Alex", parsed[0].senderName)
        assertEquals("Hello from Alex!", parsed[0].text)
        assertEquals("Sam", parsed[1].senderName)
        assertEquals("Hey Alex! So glad to sync up 💚", parsed[1].text)
    }

    @Test
    fun testWhatsAppBracketFormatParsing() {
        val sample = "[11/10/26, 14:32:10] Alex: Stargazing tonight?"
        val parsed = WhatsAppParser.parseChatExport(sample)
        assertEquals(1, parsed.size)
        assertEquals("Stargazing tonight?", parsed[0].text)
    }
}
