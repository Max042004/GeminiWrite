package com.example.geminiwithclaude.Viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State


class EnglishWritingViewModel() : ViewModel() {
    data class EnglishWritingState(
        val outputText: String = ""
    )
    private val _state = MutableStateFlow(EnglishWritingState())
    val state: StateFlow<EnglishWritingState> = _state.asStateFlow()
    private val geminiApiClient = GeminiApiClient(com.example.geminiwithclaude.BuildConfig.GEMINI_API_KEY)
    var InputText by mutableStateOf("")
        private set
    fun updateinputtext(inputext: String){
        InputText = inputext
    }

    fun processInputText(){
        viewModelScope.launch {
            val outputText = geminiApiClient.generateText(InputText) ?: ""
            updateOutputText(outputText)
        }
    }

    private fun updateOutputText(newText: String) {
        _state.value = _state.value.copy(outputText = newText)
    }
}

class GeminiApiClient(private val apiKey: String) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    suspend fun generateText(prompt: String): String? {
        val response = generativeModel.generateContent(prompt)
        return response.text
    }
}