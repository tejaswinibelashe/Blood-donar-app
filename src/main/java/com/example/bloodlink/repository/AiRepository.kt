package com.example.bloodlink.repository

import com.google.ai.client.generativeai.GenerativeModel

class AiRepository {
    // Note: The user should provide their own Gemini API Key from Google AI Studio
    private val apiKey = "AIzaSyCfFdfS9ATuCinoEiqjWL9VakASqTr5v_Q" 
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun getChatSuggestions(chatHistory: String): List<String> {
        return try {
            val prompt = """
                You are an AI assistant for BloodLink, a blood donation app.
                Based on the following chat history between a requester and a donor, 
                suggest 3 short, helpful, and professional quick responses (max 5 words each).
                Format the output as a simple comma-separated list.
                
                Chat History:
                ${chatHistory}
            """.trimIndent()

            val response = model.generateContent(prompt)
            response.text?.split(",")?.map { it.trim().removeSurrounding("\"") } ?: emptyList()
        } catch (e: Exception) {
            listOf("Okay", "Thank you", "I'll be there")
        }
    }

    suspend fun getCompatibilityAdvice(targetGroup: String): String {
        return try {
            val prompt = "You are a medical assistant for BloodLink. Briefly explain which blood groups are compatible with $targetGroup for receiving blood and any important advice for this specific group. Keep it under 40 words."
            val response = model.generateContent(prompt)
            response.text ?: "Compatibility info currently unavailable."
        } catch (e: Exception) {
            "Please consult a doctor for compatibility details."
        }
    }

    suspend fun getAiResponse(userMessage: String): String {
        return try {
            val prompt = "You are a helpful blood donor on the BloodLink app. A requester just sent you this message: \"$userMessage\". Write a short, helpful reply (max 10 words) as if you are a real person who wants to help."
            val response = model.generateContent(prompt)
            response.text ?: "I am on my way to help!"
        } catch (e: Exception) {
            "I'll be there as soon as possible."
        }
    }
}
