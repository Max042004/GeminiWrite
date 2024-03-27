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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ArticleWritingView(
    modifier: Modifier = Modifier,
    onBacktoStartButtonClicked: () -> Unit,
    ondocumentButtonClick:() -> Unit,
    articleDataFlow:  StateFlow<Map<String,List<EnglishWritingData>>>
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
        Column(modifier=Modifier) {
            val articleDataMap by articleDataFlow.collectAsState()
            //val documentnamebutton1 = articleDataMap[document.id]
            Button(
                onClick = {ondocumentButtonClick()}
            ){
                Text(text = "The book I recently read")
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
