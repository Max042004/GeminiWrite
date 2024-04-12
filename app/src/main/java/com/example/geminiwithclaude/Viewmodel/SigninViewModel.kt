package com.example.geminiwithclaude.Viewmodel

import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.ui.theme.SIGN_IN_SCREEN
import com.example.geminiwithclaude.ui.theme.NOTES_LIST_SCREEN
import com.example.geminiwithclaude.ui.theme.SIGN_UP_SCREEN
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
            openAndPopUp(NOTES_LIST_SCREEN, SIGN_IN_SCREEN)
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }
}