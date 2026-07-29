package com.example.bloodlink.data

data class BloodRequest(
    val id: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val patientName: String = "",
    val bloodGroup: String = "",
    val hospitalName: String = "",
    val location: String = "",
    val unitsRequired: String = "1",
    val urgency: String = "", // e.g., "Normal", "Urgent", "Emergency"
    val status: String = "Pending", // "Pending", "Fulfilled", "Cancelled"
    val timestamp: Long = System.currentTimeMillis()
)
