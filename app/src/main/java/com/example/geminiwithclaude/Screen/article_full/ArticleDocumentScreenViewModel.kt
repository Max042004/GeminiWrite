package com.example.geminiwithclaude.Screen.article_full

import com.example.geminiwithclaude.SPLASH_SCREEN
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleDocumentScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
) : WriterAppViewModel() {
    val articles = storageService.Articles
    fun initialize(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(SPLASH_SCREEN)
            }
        }
    }
    fun onBackClick(openScreen: (String) -> Unit) {
        openScreen("$WRITING_RECORD_SCREEN")
    }
}