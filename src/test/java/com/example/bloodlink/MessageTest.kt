package com.example.bloodlink

import com.example.bloodlink.data.Message
import org.junit.Assert.*
import org.junit.Test

class MessageTest {

    @Test
    fun testMessageDefaults() {
        val msg = Message()
        assertEquals("", msg.id)
        assertEquals("", msg.senderId)
        assertEquals("", msg.receiverId)
        assertEquals("", msg.text)
        assertTrue(msg.timestamp > 0)
    }

    @Test
    fun testMessageCreation() {
        val msg = Message(
            id = "msg_001",
            senderId = "user_A",
            receiverId = "user_B",
            text = "Need B+ blood urgently!"
        )
        assertEquals("msg_001", msg.id)
        assertEquals("user_A", msg.senderId)
        assertEquals("user_B", msg.receiverId)
        assertEquals("Need B+ blood urgently!", msg.text)
    }
}
