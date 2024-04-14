package com.example.geminiwithclaude.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField


@Composable
fun WriterScreen(
        modifier: Modifier = Modifier,
        inputText:String,
        onValueChangeA :(String) -> Unit = {},
        processInputText:() -> Unit = {},
        outputText:String,
        onRecordButtonClicked: () -> Unit,
        documentTitle:String,
        onValueChangeD:(String)-> Unit = {}
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //Change the UIview
            Button(
                onClick = { onRecordButtonClicked() },
            ){
                Text(text = "Record")
            }
            Spacer(modifier = Modifier.height(2.dp))
            //documnet title
            TextField(value = documentTitle,
                onValueChange = onValueChangeD,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your text here") },
                label = { Text("Article title") },
                maxLines = 2,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            //type input article
            OutlinedTextField(
                value = inputText,
                onValueChange = onValueChangeA,
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
                    processInputText()
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
            Text(
                text = outputText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        }
}