package com.example.geminiwithclaude.Screen.sign_in

import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.SIGN_IN_SCREEN
import com.example.geminiwithclaude.SIGN_UP_SCREEN
import com.example.geminiwithclaude.WRITING_RECORD_SCREEN
import com.example.geminiwithclaude.WRITING_SCREEN
import com.example.geminiwithclaude.WriterAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
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

    fun onGoogleSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signIn(email.value, password.value)
            openAndPopUp(WRITING_SCREEN, SIGN_IN_SCREEN)
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }
}