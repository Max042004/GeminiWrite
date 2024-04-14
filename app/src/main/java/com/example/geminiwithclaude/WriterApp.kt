package com.example.geminiwithclaude

import android.window.SplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.geminiwithclaude.Screen.SignUpScreen
import com.example.geminiwithclaude.Screen.ArticleWritingView
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import com.example.geminiwithclaude.ui.theme.Geminiwritingscreen
import com.example.geminiwithclaude.Screen.SignInScreen
import com.example.geminiwithclaude.Screen.WriterScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WriterApp() {
    GeminiwithClaudeTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    notesGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        WriterAppState(navController)
    }
//composable("name")是指該composable的name是甚麼，有name才能叫他
fun NavGraphBuilder.notesGraph(appState: WriterAppState) {
    composable(WRITING_RECORD_SCREEN) {
        ArticleWritingView(
            onBacktoStartButtonClicked = {navController.navigate(Geminiwritingscreen.Home.name)},
            articleDataFlow = appviewModel.articleData,
            onDocumentButtonClick = {documentName ->
                navController.navigate("${Geminiwritingscreen.ArticleDocument.name}/$documentName")
            },
            onDeleteButtonClicked = {selectedDocuments ->
                appviewModel.deleteDocuments(selectedDocuments)}
        )
    }

    composable(
        route = "$WRITER_ID={$WRITER_ID}",
        arguments = listOf(navArgument(WRITER_ID) { defaultValue = WRITER_DEFAULT_ID })
    ) {
        WriterScreen(
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

    composable(SIGN_IN_SCREEN) {
        SignInScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
}