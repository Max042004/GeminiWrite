package com.example.geminiwithclaude.model.Service

import com.example.geminiwithclaude.model.DeleteAccountResponse
import com.example.geminiwithclaude.model.FirebaseSignInResponse
import com.example.geminiwithclaude.model.OneTapSignInResponse
import com.example.geminiwithclaude.model.SignOutResponse
import com.example.geminiwithclaude.model.User
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean

    suspend fun verifyGoogleSignIn(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun onTapSignIn(): OneTapSignInResponse
    suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse

    suspend fun authorizeGoogleSignIn(): String?

    fun checkNeedsReAuth(): Boolean
    suspend fun signUp(email: String, password: String)
    suspend fun signOut() :SignOutResponse
    suspend fun deleteAccount()

    suspend fun deleteUserAccount(googleIdToken: String?) : DeleteAccountResponse
}