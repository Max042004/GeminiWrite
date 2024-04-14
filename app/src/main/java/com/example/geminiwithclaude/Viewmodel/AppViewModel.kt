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
import androidx.core.app.ActivityCompat.startActivityForResult
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


//良好的viewmodel程式碼，是每一個UI畫面配一個viewmodel，並且在每一個UI程式碼中instantiate viewmodel，
//因為每一個viewmodel都是配合相應的UI，所以不會有我先前寫的狀況，每一個UI畫面都instantiate 同樣的viewmodel一次
//導致我做出把instantiate上提到navigate screen程式碼中
//
class EnglishWritingViewModel() : ViewModel() {
    private val _state = MutableStateFlow(EnglishWritingState())
    val state: StateFlow<EnglishWritingState> = _state.asStateFlow()
    private val db = Firebase.firestore
    data class EnglishWritingState(
        val outputText: String = ""
    )
    data class EnglishWritingNameState(
        val name: String= ""
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

    private val _articleData = MutableStateFlow<Map<String, MutableList<EnglishWritingData>>>(emptyMap())
    val articleData: StateFlow<Map<String, MutableList<EnglishWritingData>>> = _articleData.asStateFlow()
    private val _articleNames = MutableStateFlow<List<String>>(emptyList())
    val articleNames = _articleNames.asStateFlow()
    private lateinit var registrationTokens: List<ListenerRegistration>
    fun fetchAndListenForArticleData(){
        viewModelScope.launch {
            db.collection("englishWritingData")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(ContentValues.TAG, "Error getting documents: ", e)
                        return@addSnapshotListener
                    }
                    val articleDataMap = mutableMapOf<String, MutableList<EnglishWritingData>>()
                    val articleDataNames = mutableListOf<String>()
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
                            articleDataNames.add(documentId)
                        }
                    }
                    viewModelScope.launch {
                        _articleData.emit(articleDataMap)
                        Log.d(TAG,"Emitting")
                }
                    if(articleData.value.isEmpty()){
                        Log.d(TAG,"empty map")
                    }
                    else{
                        Log.d(TAG,"contain data")
                        Log.d(TAG,articleData.value.keys.toString())
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
                    Log.d(ContentValues.TAG, "fetching new data")
                }

            })
    }

    fun stopListening() {
        registrationTokens.forEach { it.remove() }
    }

    fun deleteDocuments(documentsToDelete: List<String>) {
        viewModelScope.launch {
            if (documentsToDelete.isNotEmpty()){
                Log.d(TAG,"Contain want deleted data : ${documentsToDelete[0]}")
            }
            else{
                Log.d(TAG,"not contain want deleted data")
            }
            try {
                db.runBatch { batch ->
                    documentsToDelete.forEach { documentId ->
                        val documentRef = db.collection("englishWritingData").document(documentId)
                        batch.delete(documentRef)
                    }
                }.await()
                // 成功刪除
                Log.d(TAG, "Documents deleted successfully: $documentsToDelete")
            } catch (e: Exception) {
                // 處理錯誤
                Log.e(TAG, "Error deleting documents: $documentsToDelete", e)
            }
        }
    }
    companion object {
        private const val TAG = "EnglishWritingViewModel"
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

    //Sign in
    val authProvider: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    val authListener: FirebaseAuth.AuthStateListener =
        FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user: FirebaseUser? = auth.currentUser
            if (user == null) {
                val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(authProvider)
                    .setAlwaysShowSignInMethodScreen(true)
                    .setIsSmartLockEnabled(false)
                    .build()
                startActivityForResult(intent, this.RC_SIGN_IN)
            } else {
                this.firebaseUser = user
                displayInfo()
            }
        }
    FirebaseAuth.getInstance().addAuthStateListener(authListener)
}


