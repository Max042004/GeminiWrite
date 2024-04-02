package com.example.geminiwithclaude.ui.theme

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.geminiwithclaude.Viewmodel.AuthState
import com.example.geminiwithclaude.Viewmodel.AuthViewModel
import com.google.android.gms.common.SignInButton

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = viewModel(),
    navigateToHomeScreen: () -> Unit
) {
    val signInRequestCode = 1

    val authResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleSignInResult(result.data)
        }
    }

    val authState by viewModel.authState.collectAsState()

    when (authState) {
        AuthState.Authenticated -> navigateToHomeScreen()
        AuthState.Unauthenticated -> {
            SignInButton(
                onClick = {
                    val signInIntent = viewModel.getSignInIntent()
                    authResultLauncher.launch(signInIntent)
                }
            )
        }
        AuthState.Loading -> CircularProgressIndicator()
    }
}

@Composable
fun MyApp(
    viewModel: AuthViewModel = viewModel(),
    navigateToHomeScreen: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> navigateToHomeScreen()
            AuthState.Unauthenticated -> {
                // Do nothing, the SignInScreen will be displayed
            }
            AuthState.Loading -> {
                // Show a loading indicator
            }
        }
    }

    SignInScreen(viewModel, navigateToHomeScreen)
}

@Preview
@Composable
fun previewfun(){
    SignInScreen(navigateToHomeScreen = {})
}