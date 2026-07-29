package com.example.bloodlink.data

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val bloodGroup: String = "",
    val location: String = "",
    val profileImageUrl: String = "",
    val isDonor: Boolean = false,
    val isAdmin: Boolean = false
)
