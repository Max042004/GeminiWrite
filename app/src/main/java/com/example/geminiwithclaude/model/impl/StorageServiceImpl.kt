package com.example.geminiwithclaude.model.impl

import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import com.example.geminiwithclaude.model.Writer
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(private val auth: AccountService) : StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val Articles: Flow<List<Writer>>
        get() =
            auth.currentUser.flatMapLatest { article ->
                Firebase.firestore
                    .collection(ARTICLES_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, article?.id)
                    .dataObjects()
            }

    override suspend fun createArticle(article: Writer) {
        val articleWithUserId = article.copy(userId = auth.currentUserId)
        Firebase.firestore
            .collection(ARTICLES_COLLECTION)
            .add(articleWithUserId).await()
    }

    override suspend fun readArticle(articleId: String): Writer? {
        return Firebase.firestore
            .collection(ARTICLES_COLLECTION)
            .document(articleId).get().await().toObject()
    }

    override suspend fun updateArticle(article: Writer) {
        Firebase.firestore
            .collection(ARTICLES_COLLECTION)
            .document(article.id).set(article).await()
    }

    override suspend fun deleteArticle(articleId: String) {
        Firebase.firestore
            .collection(ARTICLES_COLLECTION)
            .document(articleId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val ARTICLES_COLLECTION = "articles"
    }
}