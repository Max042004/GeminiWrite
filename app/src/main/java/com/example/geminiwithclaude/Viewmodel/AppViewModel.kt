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


