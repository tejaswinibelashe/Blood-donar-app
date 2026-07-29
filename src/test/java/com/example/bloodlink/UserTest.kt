package com.example.bloodlink

import com.example.bloodlink.data.User
import org.junit.Assert.*
import org.junit.Test

class UserTest {

    @Test
    fun testUserDefaultValues() {
        val user = User()
        assertEquals("", user.uid)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("", user.phone)
        assertEquals("", user.bloodGroup)
        assertEquals("", user.location)
        assertEquals("", user.profileImageUrl)
        assertFalse(user.isDonor)
        assertFalse(user.isAdmin)
    }

    @Test
    fun testUserCustomValues() {
        val user = User(
            uid = "user123",
            name = "John Doe",
            email = "john@example.com",
            phone = "+1234567890",
            bloodGroup = "O+",
            location = "Chennai",
            profileImageUrl = "https://example.com/photo.jpg",
            isDonor = true,
            isAdmin = false
        )
        assertEquals("user123", user.uid)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
        assertEquals("+1234567890", user.phone)
        assertEquals("O+", user.bloodGroup)
        assertEquals("Chennai", user.location)
        assertTrue(user.isDonor)
        assertFalse(user.isAdmin)
    }

    @Test
    fun testUserCopy() {
        val user = User(uid = "u1", name = "Alice", bloodGroup = "A+")
        val updatedUser = user.copy(phone = "9876543210", isDonor = true)

        assertEquals("u1", updatedUser.uid)
        assertEquals("Alice", updatedUser.name)
        assertEquals("A+", updatedUser.bloodGroup)
        assertEquals("9876543210", updatedUser.phone)
        assertTrue(updatedUser.isDonor)
    }
}
