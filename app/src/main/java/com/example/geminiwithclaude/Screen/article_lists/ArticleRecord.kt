package com.example.geminiwithclaude.Screen.article_lists

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import android.content.ContentValues.TAG
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.model.Writer


@Composable
fun ArticleWritingView(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: ArticleRecordViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    val articles by viewModel.articles.collectAsState(emptyList())
    val showDeleteDialog = remember { mutableStateOf(false) }
    val selectedDocuments = remember { mutableStateListOf<String>() }
    var articlestitle = mutableListOf("")
    articles.forEach{article->
        articlestitle.add(article.title)
    }


    /*if (showDeleteDialog.value) {
        DeleteDocumentDialog(
            documents = articlestitle,
            selectedDocuments = selectedDocuments,
            onDismissRequest = { showDeleteDialog.value = false },
            onDeleteConfirmed = { onDeleteButtonClicked(selectedDocuments)}
        )
    }*/

    Column(modifier = Modifier) {
        Row(modifier = Modifier.fillMaxWidth()){
            Button(
            onClick={viewModel.onBackClick { openScreen(WRITING_SCREEN) }}
        ){
            Text(
                text = "Back"
            )
        }
            /*Button(onClick = { showDeleteDialog.value = true}) {
                Text(text = "Delete Documents")
            }*/
        }
            Log.d(TAG,"UI complete")
    Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (articles.isNotEmpty()) {
                items(articles, key = {it.id}) { articleItem ->
                    ArticleItem(article = articleItem, onActionClick =  {viewModel.onArticleClick(openScreen, articleItem)} )
                }
            } else {
                item {
                    Text(text = "Loading...")
                    Log.d(TAG, "false fetching")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ArticleItem(
    article: Writer,
    onActionClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onActionClick(article.id) }
        ) {
            Text(
                text = article.title,
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 12.dp),
                //style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/*@Composable
fun DeleteDocumentDialog(
    documents: List<String>,
    selectedDocuments: SnapshotStateList<String>,
    onDismissRequest: () -> Unit,
    onDeleteConfirmed: (List<String>) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Documents to Delete") },
        text = {
            Column {
                documents.forEach { document ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedDocuments.contains(document)) {
                                    selectedDocuments.remove(document)
                                } else {
                                    selectedDocuments.add(document)
                                }
                            }
                    ) {
                        Checkbox(
                            checked = selectedDocuments.contains(document),
                            onCheckedChange = null
                        )
                        Text(text = document)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteConfirmed(selectedDocuments)
                    onDismissRequest()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}*/

