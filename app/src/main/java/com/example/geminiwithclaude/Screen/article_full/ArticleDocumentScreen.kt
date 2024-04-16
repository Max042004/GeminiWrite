package com.example.geminiwithclaude.Screen.article_full

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.geminiwithclaude.WRITING_SCREEN

@Composable
fun ArticleDocumentScreen(
    modifier:Modifier = Modifier,
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: ArticleDocumentScreenViewModel = hiltViewModel()
    ){

    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    val articles by viewModel.articles.collectAsState(emptyList())

    Column {
        Button(
            onClick={viewModel.onBackClick { openScreen(WRITING_RECORD_SCREEN) }}
        ){
            Text(
                text = "Back"
            )
        }
        if (articles.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                ArticleItem(
                    inputText = article.inputtext,
                    outputText = article.outputtext
                )
            }
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
private fun ArticleItem(
    inputText: String,
    outputText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Input Text:"
        )
        Text(
            text = inputText,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Output Text:",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = outputText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}