package com.example.geminiwithclaude

import android.util.Log
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
import com.example.geminiwithclaude.Screen.article_full.ArticleDocumentScreen
import com.example.geminiwithclaude.Screen.sign_up.SignUpScreen
import com.example.geminiwithclaude.Screen.article_lists.ArticleWritingView
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import com.example.geminiwithclaude.Screen.sign_in.SignInScreen
import com.example.geminiwithclaude.Screen.article_write.WriterScreen
import com.example.geminiwithclaude.Screen.splash.SplashScreen


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
            openScreen = { route -> appState.navigate(route) },
            restartApp = { route -> appState.clearAndNavigate(route) },

        )
    }

    composable(
        route = "$WRITING_FULL_SCREEN$WRITER_ID_ARG",
        arguments = listOf(navArgument(WRITER_ID) { defaultValue = WRITER_DEFAULT_ID })
    ){
        ArticleDocumentScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(WRITING_SCREEN) {
        WriterScreen(
            modifier = Modifier
                .fillMaxSize(),
            openScreen = { route -> appState.navigate(route) },
            restartApp = { route -> appState.clearAndNavigate(route) }
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
        Log.d(TAG_TEST_SCREEN,"splash create")
    }
}