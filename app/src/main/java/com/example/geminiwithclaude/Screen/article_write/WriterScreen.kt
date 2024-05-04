package com.example.geminiwithclaude.Screen.article_write

import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil.setContentView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.Screen.sign_in.SignInScreen
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WriterScreen(
        restartApp: (String) -> Unit,
        openScreen: (String) -> Unit,
        viewModel: WriterScreenViewModel = hiltViewModel()
    ) {
    val article = viewModel.writer.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            //Change the UIview
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
                Button(
                onClick = { viewModel.onRecordClick { openScreen(WRITING_RECORD_SCREEN) } },
                ){
                Text(text = stringResource(R.string.record))
                }
                Button(
                onClick = { openAlertDialog.value = true}){
                Text(text = stringResource(R.string.sign_out))
                }
            when {
                openAlertDialog.value ->{
                    AlertDialog(title = {
                    Text(text = stringResource(R.string.sign_out_title))
                },
                    text = {
                        Text(text = stringResource(R.string.sign_out_description))
                    },
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.onSignOutClick() }
                        ) { Text("Confirm") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openAlertDialog.value = false
                            }
                        ) {
                            Text("Dismiss")
                        }
                    }
                )
                }


        }
        }
            //documnet title
            TextField(value = article.value.title,
                onValueChange = {viewModel.updatadocumenttitle(it)},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("") },
                label = { Text("Title") },
                maxLines = 2,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            )
            Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ){
        //type input article
                OutlinedTextField(
                value = article.value.inputtext,
                onValueChange = {viewModel.updateinputtext(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                placeholder = { Text("Enter your article here") },
                label = { Text("Article") },
                maxLines = 10,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(1.dp))
            SelectionContainer{
                Text(
                    text = article.value.outputtext,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )}
            }
            Button(
                onClick = {
                    // Call LLM API and update the output text
                    viewModel.processInputText()
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "AI Process Text",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WritingPreview() {
    GeminiwithClaudeTheme {
        WriterScreen({},{})
    }
}