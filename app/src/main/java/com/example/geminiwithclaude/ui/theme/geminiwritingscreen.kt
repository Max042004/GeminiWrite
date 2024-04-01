package com.example.geminiwithclaude.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import android.util.Log
import android.content.ContentValues.TAG

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
    val articleData by appviewModel.articleData.collectAsState()
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
            Log.d(TAG,"record view called")
            ArticleWritingView(
                onBacktoStartButtonClicked = {navController.navigate(Geminiwritingscreen.Start.name)},
                articleDataFlow = appviewModel.articleData,
                onDocumentButtonClick = {documentName ->
                    navController.navigate("${Geminiwritingscreen.ArticleDocument.name}/$documentName")
                },
                onDeleteButtonClicked = {selectedDocuments ->
                    appviewModel.deleteDocuments(selectedDocuments)}
            )

        }
        composable(route = "${Geminiwritingscreen.ArticleDocument.name}/{documentName}",
            arguments = listOf(navArgument("documentName") { type= NavType.StringType })
        ) { backStackEntry ->
            val documentName = backStackEntry.arguments?.getString("documentName")
            if (documentName != null) {
                ArticleDocumentScreen(
                    modifier = Modifier,
                    documentName = documentName,
                    articleDataFlow = appviewModel.articleData,
                    onBacktoStartButtonClicked = {navController.navigate(Geminiwritingscreen.ArticleRecord.name)},
                )
            }
        }
    }
}