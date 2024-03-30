package com.example.geminiwithclaude.ui.theme

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel.EnglishWritingData
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ArticleDocumentScreen(
    modifier:Modifier = Modifier,
    documentName:String,
    articleDataFlow:  StateFlow<Map<String,List<EnglishWritingData>>>,
    onBacktoStartButtonClicked:() -> Unit
    ){
    val articleDataMap by articleDataFlow.collectAsState(initial = emptyMap())
    val articlesForDocument = articleDataMap[documentName] ?: emptyList()
    Column {
        Button(
            onClick={onBacktoStartButtonClicked()}
        ){
            Text(
                text = "Back"
            )
        }
        if (articlesForDocument.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(articlesForDocument) { article ->
                ArticleItem(
                    inputText = article.inputText,
                    outputText = article.outputText
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