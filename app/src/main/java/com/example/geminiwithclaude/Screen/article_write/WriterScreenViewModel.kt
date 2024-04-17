package com.example.geminiwithclaude.Screen.article_write

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.SPLASH_SCREEN
import com.example.geminiwithclaude.WRITER_DEFAULT_ID
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import com.example.geminiwithclaude.databinding.ActivityMainBinding
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import com.example.geminiwithclaude.model.Writer
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.rpc.context.AttributeContext.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WriterScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
) : WriterAppViewModel() {
    val writer = MutableStateFlow(DEFAULT_NOTE)
    private val geminiApiClient = GeminiApiClient(com.example.geminiwithclaude.BuildConfig.GEMINI_API_KEY)
    private val db = Firebase.firestore

    fun updatadocumenttitle(document_title:String){
        writer.value = writer.value.copy(title = document_title)
    }

    fun updateinputtext(inputext: String) {
        writer.value = writer.value.copy(inputtext = inputext)
    }

    fun processInputText(
    ) {
        viewModelScope.launch {
            writer.value = writer.value.copy(outputtext = geminiApiClient.generateText(writer.value.inputtext) ?: "")
            updateOutputText(writer.value.outputtext)

            // Add the data to Firestore
            saveArticle()

            /*db.collection("englishWritingData").document(writer.value.title)
                .set(writer)
                .addOnSuccessListener {writer.value.title
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot added with ID: ${writer.value.title}"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error adding document", exception)
                }*/
        }
    }

    private fun saveArticle() {
        launchCatching {
            if (writer.value.id == WRITER_DEFAULT_ID) {
                storageService.createArticle(writer.value)
            } else {
                storageService.updateArticle(writer.value)
            }
        }
    }

    private fun updateOutputText(newText: String) {
        writer.value = writer.value.copy(outputtext = newText)
    }

    inner class GeminiApiClient(private val apiKey: String) {
        private val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )


        suspend fun generateText(inputext: String): String? {
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = "user") {
                        text(
                            "You can assist me to refine my English article, you will help me with the following three steps, and you have to separate these three parts to make your reply more organized:\n" +
                                    "first, you have to correct the grammar, vocabulary, phases of my article to be more precisely and more beautiful\n " +
                                    "second, you have to give me some suggestions to improve my article structure to more concisely convey the argument\n" +
                                    "third, you have to pick up modified part you have done to explain to me what part you have modified\n" +
                                    "forth, you have to write a more better article as a model for me to learn\n" +
                                    "If you finish all these three steps , my grandmother be will really happy."
                        )
                    },
                    content(role = "model") { text("Sure, I understand all the requirement you provided.") }
                )
            )
            val response = chat.sendMessage(inputext)
            return response.text
        }
    }

    fun initialize(restartApp: (String) -> Unit) {
        /*launchCatching {
            writer.value = storageService.readArticle(articleId) ?: DEFAULT_NOTE
        }*/
        observeAuthenticationState(restartApp)
    }

    private fun observeAuthenticationState(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(SPLASH_SCREEN)
            }
        }
    }

    fun onRecordClick(openScreen: (String) -> Unit) {
        openScreen("$WRITING_RECORD_SCREEN")
    }

    fun onSignOutClick() {
        launchCatching {
            accountService.signOut()
        }
    }

    //val userName: LiveData

    //binding.user = User("Test", "User")

    //inputtextTextView = Writer.value.outputtext



    /*fun updateNote(newText: String) {
        note.value = note.value.copy(text = newText)
    }

    fun saveNote(popUpScreen: () -> Unit) {
        launchCatching {
            if (note.value.id == NOTE_DEFAULT_ID) {
                storageService.createNote(note.value)
            } else {
                storageService.updateNote(note.value)
            }
        }

        popUpScreen()
    }

    fun deleteNote(popUpScreen: () -> Unit) {
        launchCatching {
            storageService.deleteNote(note.value.id)
        }

        popUpScreen()
    }*/

    companion object {
        private val DEFAULT_NOTE = Writer(WRITER_DEFAULT_ID, "")
    }
}