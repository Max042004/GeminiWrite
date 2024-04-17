package com.example.geminiwithclaude.Screen.article_full

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.model.Writer
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp


@Composable
fun ArticleDocumentScreen(
    articleId:String,
    modifier:Modifier = Modifier,
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: ArticleDocumentScreenViewModel = hiltViewModel()
    ) {

    LaunchedEffect(Unit) { viewModel.initialize(articleId, restartApp) }

    val article = viewModel.articles.collectAsState().value

    Column {
        Button(
            onClick = { viewModel.onBackClick { openScreen(WRITING_RECORD_SCREEN) } }
        ) {
            Text(
                text = "Back"
            )
        }
        if (article.inputtext.isNotEmpty()) {
            SelectionContainer {
                ArticleItem(
                    inputText = article.inputtext,
                    outputText = article.outputtext
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No articles found for the selected document")
            }
        }
    }
}

@Composable
fun ArticleItem(
    inputText: String,
    outputText : String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Input Text:",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 24.sp,
        )
        Text(
            text = inputText,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp

        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Output Text:",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 24.sp
        )
        Text(
            text = outputText,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
    }
}