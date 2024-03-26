package com.example.geminiwithclaude.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun ArticleWritingView(
    modifier: Modifier = Modifier,
    onBacktoStartButtonClicked: () -> Unit,
    articlerecordMap: MutableMap<String,List<EnglishWritingData>>
){
    Column {
        Text(
            text = "Success"
        )
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
        /*ArticleDataView(
            modifier = modifier,
            articleDataList = articlerecordList
        )*/
    }
}

@Composable
fun ArticledocumentView(
    modifier: Modifier = Modifier
){
    Column(modifier=Modifier) {
        Button(
            onClick =
        ){
            Text(text = "Document name")
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
