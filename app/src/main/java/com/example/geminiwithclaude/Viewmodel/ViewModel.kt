package com.example.geminiwithclaude.Viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.content.ContentValues.TAG
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class EnglishWritingViewModel() : ViewModel() {
    private val _state = MutableStateFlow(EnglishWritingState())
    val state: StateFlow<EnglishWritingState> = _state.asStateFlow()
    private val db = Firebase.firestore
    data class EnglishWritingState(
        val outputText: String = ""
    )

    data class EnglishWritingData(
        val inputText: String = "",
        val outputText: String = ""
    )
    private val geminiApiClient = GeminiApiClient(com.example.geminiwithclaude.BuildConfig.GEMINI_API_KEY)
    var InputText by mutableStateOf("")
        private set

    fun updateinputtext(inputext: String) {
        InputText = inputext
    }

    var documenttitle by mutableStateOf("")
        private set
    fun updatadocumenttitle(document_title:String){
        documenttitle = document_title
    }
    fun processInputText(
    ) {
        viewModelScope.launch {
            val outputText = geminiApiClient.generateText(InputText) ?: ""
            updateOutputText(outputText)
            // Create an instance of EnglishWritingData
            val englishWritingData = EnglishWritingData(InputText, outputText)

            // Add the data to Firestore
            //已可成功上傳一組input output text到firebase
            db.collection("englishWritingData").document(documenttitle)
                .set(englishWritingData)
                .addOnSuccessListener {documenttitle
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot added with ID: $documenttitle"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error adding document", exception)
                }
        }
    }

    private val _articleData =MutableSharedFlow<Map<String, MutableList<EnglishWritingData>>>(1)
    val articleData =_articleData.asSharedFlow()
    private lateinit var registrationTokens: List<ListenerRegistration>

    fun fetchAndListenForArticleData() {
        viewModelScope.launch {
            db.collection("englishWritingData")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(ContentValues.TAG, "Error getting documents: ", e)
                        return@addSnapshotListener
                    }

                    val articleDataMap = mutableMapOf<String, MutableList<EnglishWritingData>>()
                    for (document in snapshot!!.documents) {
                        val inputText = document.getString("inputText") ?: ""
                        val outputText = document.getString("outputText") ?: ""
                        val data = EnglishWritingData(inputText, outputText)
                        val documentId = document.id
                        Log.d(ContentValues.TAG, "fetching data ")
                        if (articleDataMap.containsKey(documentId)) {
                            articleDataMap[documentId]?.add(data)
                        } else {
                            articleDataMap[documentId] = mutableListOf(data)
                        }
                    }
                    viewModelScope.launch {
                        _articleData.emit(articleDataMap)
                }
        }
    }
    }
    fun startListening() {
        registrationTokens = listOf(db.collection("englishWritingData")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d(ContentValues.TAG, "Error getting documents: ", e)
                    return@addSnapshotListener
                }

                val articleDataMap = mutableMapOf<String, MutableList<EnglishWritingData>>()
                for (document in snapshot!!.documents) {
                    val inputText = document.getString("inputText") ?: ""
                    val outputText = document.getString("outputText") ?: ""
                    val data = EnglishWritingData(inputText, outputText)
                    val documentId = document.id
                    when (document.metadata.isFromCache) {
                        true -> {
                            // Data is from cache, no updates
                        }

                        false -> {
                            // Data has changed
                            when (document.metadata.hasPendingWrites()) {
                                true -> {
                                    // Document has been updated
                                    if (articleDataMap.containsKey(documentId)) {
                                        articleDataMap[documentId]?.clear()
                                        articleDataMap[documentId]?.add(data)
                                    } else {
                                        articleDataMap[documentId] = mutableListOf(data)
                                    }
                                }

                                false -> {
                                    // Document has been added or removed
                                    if (articleDataMap.containsKey(documentId)) {
                                        articleDataMap.remove(documentId)
                                    } else {
                                        articleDataMap[documentId] = mutableListOf(data)
                                    }
                                }
                            }
                        }
                    }
                }
                viewModelScope.launch {
                    _articleData.emit(articleDataMap)
                }
            })
    }

    fun stopListening() {
        registrationTokens.forEach { it.remove() }
    }

    private fun updateOutputText(newText: String) {
        _state.value = _state.value.copy(outputText = newText)
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
}


    /*fun fetchArticleData() {
        viewModelScope.launch {
            db.collection("englishWritingData")
                .get()
                .addOnSuccessListener { documents ->
                    val articleDataMap = mutableMapOf<String, MutableList<EnglishWritingData>>()
                    for (document in documents) {
                        val inputText = document.getString("inputText") ?: ""
                        val outputText = document.getString("outputText") ?: ""
                        val data = EnglishWritingData(inputText, outputText)
                        val documentId = document.id
                        if (articleDataMap.containsKey(documentId)) {
                            articleDataMap[documentId]?.add(data)
                        } else {
                            articleDataMap[documentId] = mutableListOf(data)
                        }
                    }
                    _articleData.value = articleDataMap}
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    // Handle any errors
                }
        }
    }*/
    /*update firestore data change
    fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {

        // Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e)
            return
        }

        // Dispatch the event
        if (documentSnapshots != null) {
            for (change in documentSnapshots.documentChanges) {
                // snapshot of the changed document
                when (change.type) {
                    DocumentChange.Type.ADDED -> {
                        // TODO: handle document added
                    }
                    DocumentChange.Type.MODIFIED -> {
                        // TODO: handle document changed
                    }
                    DocumentChange.Type.REMOVED -> {
                        // TODO: handle document removed
                    }
                }
            }
        }
        onDataChanged()
    }
    private fun onDocumentAdded(change: DocumentChange) {
        val articleDataMap = mutableMapOf<String, MutableList<EnglishWritingData>>()
        snapshots.add(change., change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }*/
    /*fun getarticledata(){
        db.collection("englishWritingData")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }*/


    /*suspend fun generateText(prompt: String): String? {
        val response = generativeModel.generateContent(prompt)
        return response.text
    }
    }*/

