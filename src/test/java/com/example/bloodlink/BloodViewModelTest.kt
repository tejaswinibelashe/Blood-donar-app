package com.example.bloodlink

import com.example.bloodlink.ui.viewmodels.BloodViewModel
import com.example.bloodlink.ui.viewmodels.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BloodViewModelTest {

    private lateinit var viewModel: BloodViewModel

    @Before
    fun setUp() {
        viewModel = BloodViewModel()
    }

    @Test
    fun testInitialRequestStateIsIdle() {
        assertEquals(RequestState.Idle, viewModel.requestState.value)
    }

    @Test
    fun testResetState() {
        viewModel.resetState()
        assertEquals(RequestState.Idle, viewModel.requestState.value)
    }

    @Test
    fun testDefaultHospitalsLoaded() = runBlocking {
        delay(200)
        val hospitals = viewModel.nearbyHospitals.value
        assertNotNull(hospitals)
        assertTrue(hospitals.isNotEmpty())
    }
}
