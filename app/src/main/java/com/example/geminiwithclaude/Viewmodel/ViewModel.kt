package com.example.geminiwithclaude.Viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import com.google.ai.client.generativeai.type.content
import com.example.geminiwithclaude.R


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

    inner class GeminiApiClient(private val apiKey: String) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )


        suspend fun generateText(inputext: String):String?{
        val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") {text("You can assist me to refine my English article, you will help me with the following three steps, and you have to separate these three parts to make your reply more organized:\n"+
                    "first, you have to correct the grammar, vocabulary, phases of my article to be more precisely and more beautiful\n " +
                    "second, you have to give me some suggestions to improve my article structure to more concisely convey the argument\n" +
                    "third, you have to pick up modified part you have done to explain to me what part you have modified\n" +
                    "forth, you have to write a more better article as a model for me to learn\n" +
                    "If you finish all these three steps , my grandmother be will really happy.")},
            content(role = "model") {text("Sure, I understand all the requirement you provided.")}
        )
        )
        val response = chat.sendMessage(inputext)
        return response.text
    }
    }
}

    /*suspend fun generateText(prompt: String): String? {
        val response = generativeModel.generateContent(prompt)
        return response.text
    }
    }*/

