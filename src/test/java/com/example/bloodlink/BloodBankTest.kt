package com.example.bloodlink

import com.example.bloodlink.data.BloodBank
import org.junit.Assert.*
import org.junit.Test

class BloodBankTest {

    @Test
    fun testBloodBankDefaults() {
        val bank = BloodBank()
        assertEquals("", bank.id)
        assertEquals("", bank.name)
        assertEquals("", bank.location)
        assertEquals("", bank.phone)
        assertTrue(bank.stock.isEmpty())
    }

    @Test
    fun testBloodBankStock() {
        val stockMap = mapOf(
            "A+" to 20,
            "O+" to 15,
            "B-" to 5
        )
        val bank = BloodBank(
            id = "bb_1",
            name = "Central Blood Bank",
            location = "Main St",
            phone = "123-456",
            stock = stockMap
        )

        assertEquals(20, bank.stock["A+"])
        assertEquals(15, bank.stock["O+"])
        assertEquals(5, bank.stock["B-"])
        assertNull(bank.stock["AB-"])
    }
}
