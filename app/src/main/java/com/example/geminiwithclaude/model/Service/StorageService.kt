package com.example.geminiwithclaude.model.Service

import com.example.geminiwithclaude.model.Writer
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val Articles: Flow<List<Writer>>
    suspend fun createArticle(article: Writer)
    suspend fun readArticle(articleId: String): Writer?
    suspend fun updateArticle(article: Writer)
    suspend fun deleteArticle(articleId: String)
}