package com.example.geminiwithclaude.Screen.article_full

import com.example.geminiwithclaude.SPLASH_SCREEN
import com.example.geminiwithclaude.WRITER_DEFAULT_ID
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import com.example.geminiwithclaude.model.Writer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleDocumentScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
) : WriterAppViewModel() {
    //val articles = storageService.Articles
    val articles = MutableStateFlow(DEFAULT_NOTE)
    fun initialize(articleId: String, restartApp: (String) -> Unit) {
        launchCatching {
            articles.value = storageService.readArticle(articleId) ?: DEFAULT_NOTE
        }

        observeAuthenticationState(restartApp)
    }

    private fun observeAuthenticationState(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(SPLASH_SCREEN)
            }
        }
    }
    fun onBackClick(openScreen: (String) -> Unit) {
        openScreen("$WRITING_RECORD_SCREEN")
    }
    companion object {
        private val DEFAULT_NOTE = Writer(WRITER_DEFAULT_ID, "My Note")
    }
}