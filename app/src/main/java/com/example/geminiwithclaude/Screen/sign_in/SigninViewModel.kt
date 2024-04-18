package com.example.geminiwithclaude.Screen.sign_in

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.SIGN_IN_SCREEN
import com.example.geminiwithclaude.SIGN_UP_SCREEN
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import com.example.geminiwithclaude.model.DataProvider
import com.example.geminiwithclaude.model.Response
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService,
    val oneTapClient: SignInClient
) : WriterAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(openAndPopUp: (String,String) -> Unit) {
        launchCatching {
            accountService.signIn(email.value, password.value)
            openAndPopUp(WRITING_SCREEN, SIGN_IN_SCREEN)
        }
    }


    fun oneTapSignIn() = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.oneTapSignInResponse = Response.Loading
        DataProvider.oneTapSignInResponse = accountService.onTapSignIn()
    }

    fun onGoogleSignInClick(credentials: SignInCredential,openAndPopUp: (String,String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.googleSignInResponse = Response.Loading
        DataProvider.googleSignInResponse = accountService.signInWithGoogle(credentials)
        val signInResponse = DataProvider.googleSignInResponse

            when (signInResponse) {
            is Response.Success -> {
                Log.d(TAG, "log1")
                delay(500)
                openAndPopUp(WRITING_SCREEN, SIGN_IN_SCREEN)
                Log.d(TAG, "log2")
            }
            is Response.Failure -> {
                // Handle sign-in failure
                Log.e(TAG, "Sign-in failed: ${signInResponse.e}")
            }
                else ->{
                        Log.d(TAG,"log3")
                    }
        }
    }

    fun onSignUpClick(openAndPopUp: (String,String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }

    fun checkNeedsReAuth() = CoroutineScope(Dispatchers.IO).launch {
        if (accountService.checkNeedsReAuth()) {
            // Authorize google sign in
            val idToken = accountService.authorizeGoogleSignIn()
            if (idToken != null) {
                deleteAccount(idToken)
            }
            else {
                // If failed initiate oneTap sign in flow
                // deleteAccount(googleIdToken:) will be called from oneTap result callback
                oneTapSignIn()
                Log.i("deleteAccount","OneTapSignIn")
            }
        } else {
            deleteAccount(null)
        }
    }

    fun deleteAccount(googleIdToken: String?) = CoroutineScope(Dispatchers.IO).launch {
        Log.i("deleteAccount","Deleting Account...")
        DataProvider.deleteAccountResponse = Response.Loading
        DataProvider.deleteAccountResponse = accountService.deleteUserAccount(googleIdToken)
    }
}