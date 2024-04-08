package com.example.geminiwithclaude.ui.theme

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geminiwithclaude.Viewmodel.AuthState
import com.example.geminiwithclaude.Viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geminiwithclaude.R
import com.google.android.gms.common.SignInButton
import java.lang.reflect.Modifier

/*@Composable
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
            SignInButton (
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
fun StartSignInScreen(
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

@Composable
fun SignInButton(
    text: String = "Sign In with Google",
    loadingText: String = "Loading...",
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Unspecified
    )
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = onClick,
        colors = buttonColors,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google__g__logo_svg),
            contentDescription = "Google Logo",
            tint = Color.Unspecified
        )
        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
        Text(
            text = if (isLoading) loadingText else text,
            color = Color.Unspecified
        )
    }
}

@Preview
@Composable
fun previewfun(){
    SignInScreen(navigateToHomeScreen = {})
}*/