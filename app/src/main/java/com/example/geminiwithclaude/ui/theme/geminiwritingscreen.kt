package com.example.geminiwithclaude.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel

enum class Geminiwritingscreen() {
    Start,
    ArticleRecord,
    ArticleDocument
}
@Composable
fun GeminiWritingScreen(
    navController: NavHostController = rememberNavController(),
    appviewModel: EnglishWritingViewModel = viewModel(),
){
    val uiState by appviewModel.state.collectAsState()
    val articleData by appviewModel.articleData.collectAsState(initial = null)
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Geminiwritingscreen.Start.name,
        modifier = Modifier.padding(all = 8.dp)){
        composable(route = Geminiwritingscreen.Start.name){
            StartView(
                modifier = Modifier
                .fillMaxSize(),
                inputText = appviewModel.InputText,
                onValueChangeA = {appviewModel.updateinputtext(it)},
                processInputText = {appviewModel.processInputText()},
                outputText = uiState.outputText,
                onRecordButtonClicked = {navController.navigate(Geminiwritingscreen.ArticleRecord.name)},
                documentTitle = appviewModel.documenttitle,
                onValueChangeD = {appviewModel.updatadocumenttitle(it)}
            )
        }
        composable(route = Geminiwritingscreen.ArticleRecord.name) {
            val context = LocalContext.current
            ArticleWritingView(
                modifier = Modifier,
                onBacktoStartButtonClicked = {navController.navigate(Geminiwritingscreen.Start.name)},
                ondocumentButtonClick = {navController.navigate(Geminiwritingscreen.ArticleDocument.name)}
                //articlerecordList = appviewModel.articleData.collectAsState(initial = emptyList()).value
            )
        }
        composable(route = Geminiwritingscreen.ArticleDocument.name){
            ArticleDocumentScreen(
                modifier = Modifier,
                documentName = "The neighborhood",
                articleDataFlow = appviewModel.articleData
            )
        }


}
}