package com.example.bloodlink

import com.example.bloodlink.repository.BloodRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BloodRepositoryTest {

    private lateinit var repository: BloodRepository

    @Before
    fun setUp() {
        repository = BloodRepository()
    }

    @Test
    fun testGetNearbyHospitalsChennai() = runBlocking {
        val hospitals = repository.getNearbyHospitals("Chennai")
        assertNotNull(hospitals)
        assertTrue(hospitals.isNotEmpty())
        assertTrue(hospitals.any { it.hospitalName.contains("Saveetha", ignoreCase = true) })
    }

    @Test
    fun testGetNearbyHospitalsNellore() = runBlocking {
        val hospitals = repository.getNearbyHospitals("Nellore")
        assertNotNull(hospitals)
        assertTrue(hospitals.isNotEmpty())
        assertTrue(hospitals.any { it.hospitalName.contains("Medicover", ignoreCase = true) })
    }

    @Test
    fun testGetNearbyHospitalsDefault() = runBlocking {
        val hospitals = repository.getNearbyHospitals("UnknownCity")
        assertNotNull(hospitals)
        assertTrue(hospitals.isNotEmpty())
    }
}
