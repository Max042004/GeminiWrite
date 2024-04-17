package com.example.geminiwithclaude.Screen.article_write

import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.databinding.DataBindingUtil.setContentView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN


@Composable
@OptIn(ExperimentalFoundationApi::class)
fun WriterScreen(
        modifier: Modifier = Modifier,
        restartApp: (String) -> Unit,
        viewModel: WriterScreenViewModel = hiltViewModel(),
        openScreen: (String) -> Unit
    ) {
    val article = viewModel.writer.collectAsState()

    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //Change the UIview
            Button(
                onClick = { viewModel.onRecordClick { openScreen(WRITING_RECORD_SCREEN) } },
            ){
                Text(text = "Record")
            }
            Spacer(modifier = Modifier.height(2.dp))
            //documnet title
            TextField(value = article.value.title,
                onValueChange = {viewModel.updatadocumenttitle(it)},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your text here") },
                label = { Text("Article title") },
                maxLines = 2,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            //type input article
            OutlinedTextField(
                value = article.value.inputtext,
                onValueChange = {viewModel.updateinputtext(it)},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your article here") },
                label = { Text("Article input") },
                maxLines = 5,
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = {
                    // Call LLM API and update the output text
                    viewModel.processInputText()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Process Text",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
            ){
                SelectionContainer{
                Text(
                text = article.value.outputtext,
                style = MaterialTheme.typography.bodyMedium
            )}
        }
        }
}