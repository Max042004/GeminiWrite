package com.example.geminiwithclaude.Screen.sign_in

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

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signIn(email.value, password.value)
            openAndPopUp(WRITING_SCREEN, SIGN_IN_SCREEN)
        }
    }


    fun oneTapSignIn(openAndPopUp: (String, String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.oneTapSignInResponse = Response.Loading
        DataProvider.oneTapSignInResponse = accountService.onTapSignIn()
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }

    fun onGoogleSignInClick(credentials: SignInCredential) = CoroutineScope(Dispatchers.IO).launch {
        DataProvider.googleSignInResponse = Response.Loading
        DataProvider.googleSignInResponse = accountService.signInWithGoogle(credentials)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
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
                oneTapSignIn(openAndPopUp = {WRITING_SCREEN,SIGN_IN_SCREEN-> Unit})
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