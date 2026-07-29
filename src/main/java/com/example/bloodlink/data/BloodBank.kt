package com.example.bloodlink.data

data class BloodBank(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val phone: String = "",
    val stock: Map<String, Int> = emptyMap() // Blood Group to Units
)
