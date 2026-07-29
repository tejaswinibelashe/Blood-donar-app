package com.example.bloodlink

import com.example.bloodlink.repository.AiRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AiRepositoryTest {

    private lateinit var aiRepository: AiRepository

    @Before
    fun setUp() {
        aiRepository = AiRepository()
    }

    @Test
    fun testGetCompatibilityAdviceReturnsNonEmptyString() = runBlocking {
        val advice = aiRepository.getCompatibilityAdvice("O+")
        assertNotNull(advice)
        assertTrue(advice.isNotBlank())
    }

    @Test
    fun testGetChatSuggestionsReturnsList() = runBlocking {
        val suggestions = aiRepository.getChatSuggestions("Hello, are you available to donate?")
        assertNotNull(suggestions)
        assertTrue(suggestions.isNotEmpty())
    }

    @Test
    fun testGetAiResponseReturnsValidMessage() = runBlocking {
        val response = aiRepository.getAiResponse("Need A+ blood at City Hospital")
        assertNotNull(response)
        assertTrue(response.isNotBlank())
    }
}
