package com.example.geminiwithclaude.Screen.article_lists

import com.example.geminiwithclaude.SPLASH_SCREEN
import com.example.geminiwithclaude.WRITER_ID
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import com.example.geminiwithclaude.model.Writer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleRecordViewModel @Inject constructor(
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
        openScreen("$WRITING_SCREEN")
    }

    fun onArticleClick(openScreen: (String) -> Unit, article: Writer) {
        openScreen("$WRITING_SCREEN?$WRITER_ID=${article.id}")
    }
}