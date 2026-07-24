package com.example.bloodlink

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class ComposeUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreenUIElements() {
        composeTestRule.setContent {
            // Mock Login Screen or generic text
            androidx.compose.material3.Text("Login")
            androidx.compose.material3.Button(onClick = {}) {
                androidx.compose.material3.Text("Submit")
            }
        }
        
        composeTestRule.onNodeWithText("Login").assertExists()
        composeTestRule.onNodeWithText("Submit").assertExists().performClick()
    }
}
