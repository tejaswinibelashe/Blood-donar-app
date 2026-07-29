package com.example.bloodlink

import com.example.bloodlink.data.BloodRequest
import org.junit.Assert.*
import org.junit.Test

class BloodRequestTest {

    @Test
    fun testBloodRequestDefaults() {
        val request = BloodRequest()
        assertEquals("", request.id)
        assertEquals("", request.requesterId)
        assertEquals("", request.requesterName)
        assertEquals("", request.patientName)
        assertEquals("", request.bloodGroup)
        assertEquals("", request.hospitalName)
        assertEquals("", request.location)
        assertEquals("1", request.unitsRequired)
        assertEquals("", request.urgency)
        assertEquals("Pending", request.status)
        assertTrue(request.timestamp > 0)
    }

    @Test
    fun testBloodRequestCreation() {
        val request = BloodRequest(
            id = "req_101",
            requesterId = "req_user_1",
            requesterName = "Jane Smith",
            patientName = "Bob Smith",
            bloodGroup = "AB-",
            hospitalName = "Saveetha Medical College Hospital",
            location = "Poonamallee, Chennai",
            unitsRequired = "3",
            urgency = "HIGH",
            status = "Fulfilled"
        )
        assertEquals("req_101", request.id)
        assertEquals("Jane Smith", request.requesterName)
        assertEquals("Bob Smith", request.patientName)
        assertEquals("AB-", request.bloodGroup)
        assertEquals("Saveetha Medical College Hospital", request.hospitalName)
        assertEquals("Poonamallee, Chennai", request.location)
        assertEquals("3", request.unitsRequired)
        assertEquals("HIGH", request.urgency)
        assertEquals("Fulfilled", request.status)
    }
}
