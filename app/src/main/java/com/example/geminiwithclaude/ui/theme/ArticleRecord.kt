package com.example.geminiwithclaude.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel.EnglishWritingData
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.SharedFlow
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.LaunchedEffect

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ArticleWritingView(
    modifier: Modifier = Modifier,
    onBacktoStartButtonClicked: () -> Unit,
    onDocumentButtonClick:(String) -> Unit,
    articleDataFlow:  SharedFlow<Map<String,List<EnglishWritingData>>>
){
    val articleDataMap = remember { mutableStateMapOf<String, MutableList<EnglishWritingData>>() }
    val documentNames = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        articleDataFlow.collect { newData ->
            newData.forEach { (documentName, data) ->
                articleDataMap.getOrPut(documentName) { mutableListOf() }.addAll(data)
                if (!documentNames.contains(documentName)) {
                    documentNames.add(documentName)
                }
            }
        }
    }
    Column {
        Button(
            onClick={onBacktoStartButtonClicked()}
        ){
            Text(
                text = "Back"
            )
        }
        Text(
            text = "Show the article"
        )
        Column(modifier=Modifier) {
            documentNames.forEach { documentName ->
            Button(
                onClick = { onDocumentButtonClick(documentName) }, // Pass the document name to the callback
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = documentName)
            }
        }
    }
}
}




/*@Composable
fun ArticleDataView(
    modifier: Modifier = Modifier,
    articleDataList: List<EnglishWritingData>
) {
    if (articleDataList.isEmpty()) {
        // Show a loading indicator or a message while data is being fetched
        Text(text = "Loading...")
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp)
        ) {
            items(articleDataList) { articleData ->
                Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = "Input Text:",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = articleData.inputText,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Output Text:",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = articleData.outputText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
    }*/
