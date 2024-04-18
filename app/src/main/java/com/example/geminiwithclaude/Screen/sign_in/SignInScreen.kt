package com.example.geminiwithclaude.Screen.sign_in

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.model.DataProvider
import com.example.geminiwithclaude.model.Response
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import com.example.geminiwithclaude.ui.theme.Purple40
import com.example.geminiwithclaude.ui.theme.component.AuthLoginProgressIndicator
import com.example.geminiwithclaude.ui.theme.component.GoogleSignIn
import com.example.geminiwithclaude.ui.theme.component.OneTapSignIn
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignInScreen(
    openAndPopUp: (String,String) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    loginState: MutableState<Boolean>? = null
) {
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()

    // 1.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                // 2.
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                viewModel.onGoogleSignInClick(credentials, openAndPopUp)
            }
            catch (e: ApiException) {
                Log.e("LoginScreen:Launcher","Login One-tap $e")
            }
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            Log.e("LoginScreen:Launcher","OneTapClient Canceled")

        }
    }

    fun launch(signInResult: BeginSignInResult) {
        // 3.
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth_image),//之後用一個sign in 畫面的ICON
            contentDescription = "Auth image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp))

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .border(
                    BorderStroke(width = 2.dp, color = Purple40),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = email.value,
            onValueChange = { viewModel.updateEmail(it) },
            placeholder = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
        )

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .border(
                    BorderStroke(width = 2.dp, color = Purple40),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = password.value,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = { Text(stringResource(R.string.password)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Email") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp))
        Button(
            onClick = {viewModel.oneTapSignIn()},
            modifier = Modifier.wrapContentWidth(),

        ){
            Text(
                text = stringResource(R.string.sign_in_google),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }
        /*Button(
            onClick = {viewModel.onGoogleSignInClick(openAndPopUp)},
            modifier = Modifier.wrapContentWidth(),

            ){
            Text(
                text = stringResource(R.string.sign_in_google),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }*/
        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            onClick = { viewModel.onSignInClick(openAndPopUp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp))

        TextButton(onClick = { viewModel.onSignUpClick(openAndPopUp) }) {
            Text(text = stringResource(R.string.sign_up_description), fontSize = 16.sp)
        }

        OneTapSignIn (
            launch = {
                launch(it)
            }
        )

        GoogleSignIn {
            // Dismiss LoginScreen
            loginState?.let {
                it.value = false
            }
        }
    }
    when(val oneTapSignInResponse = DataProvider.oneTapSignInResponse) {
        // 1.
        is Response.Loading ->  {
            Log.i("Login:OneTap", "Loading")
            AuthLoginProgressIndicator()
        }
        // 2.
        is Response.Success -> oneTapSignInResponse.data?.let { signInResult ->
            LaunchedEffect(signInResult) {
                launch(signInResult)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:OneTap", "${oneTapSignInResponse.e}")
        }
    }

    when (val signInWithGoogleResponse = DataProvider.googleSignInResponse) {
        // 1.
        is Response.Loading -> {
            Log.i("Login:GoogleSignIn", "Loading")
            AuthLoginProgressIndicator()
        }
        // 3.
        is Response.Success -> signInWithGoogleResponse.data?.let { authResult ->
            Log.i("Login:GoogleSignIn", "Success: $authResult")
            loginState?.let { it.value = false }
        }
        is Response.Failure -> {
            Log.e("Login:GoogleSignIn", "${signInWithGoogleResponse.e}")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthPreview() {
    GeminiwithClaudeTheme {
        //SignInScreen({ "";"",_,-> })
    }
}