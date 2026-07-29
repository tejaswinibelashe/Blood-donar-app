package com.example.bloodlink

import com.example.bloodlink.ui.viewmodels.AuthState
import com.example.bloodlink.ui.viewmodels.AuthViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        viewModel = AuthViewModel()
    }

    @Test
    fun testInitialStateIsIdle() {
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    @Test
    fun testSetErrorState() {
        viewModel.setError("Test Error Message")
        assertTrue(viewModel.authState.value is AuthState.Error)
        assertEquals("Test Error Message", (viewModel.authState.value as AuthState.Error).message)
    }

    @Test
    fun testResetState() {
        viewModel.setError("Some Error")
        viewModel.resetState()
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }
}
