package com.example.geminiwithclaude.ui.theme

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel.EnglishWritingData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items



@Composable
fun ArticleWritingView(
    onBacktoStartButtonClicked: () -> Unit,
    onDocumentButtonClick:(String) -> Unit,
    articleDataFlow:  StateFlow<Map<String,List<EnglishWritingData>>>,
    //recordviewmodel :EnglishWritingViewModel = viewModel()
){
    val articleDataMap by articleDataFlow.collectAsState(initial = emptyMap())
    if(articleDataMap.isEmpty()){
        Log.d(TAG,"is empty")
    }
    else{
        Log.d(TAG,"not empty")
    }
    val articleDataNames = articleDataMap.keys.toMutableList()
    if(articleDataNames.isNotEmpty()){Log.d(TAG,"not empty list")}
    else{
        Log.d(TAG,"empty list")
    }
    Column(modifier = Modifier) {
        Button(
            onClick={onBacktoStartButtonClicked()}
        ){
            Text(
                text = "Back"
            )
        }
            Log.d(TAG,"UI complete")
    Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (articleDataNames.isNotEmpty()) {
                items(articleDataNames) { documentName ->
                    Button(
                        onClick = { onDocumentButtonClick(documentName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = documentName)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Log.d(TAG, "Button created")
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

