package com.example.geminiwithclaude.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel.EnglishWritingData

@Composable
fun articledocumentscreen(
    modifier:Modifier = Modifier,
    documentname:String,
    articleDataMap:  MutableMap<String,List<EnglishWritingData>>
    ){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items()
    }
}