package com.example.geminiwithclaude.model.impl

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import com.example.geminiwithclaude.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.example.geminiwithclaude.model.User
import com.example.geminiwithclaude.model.Service.AccountService
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AccountServiceImpl @Inject constructor() : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInwithGoogle() {
        @Provides
        fun provideOneTapClient(
            @ApplicationContext
            context: Context
        ) = Identity.getSignInClient(context)

        @Provides
        @Named("signInRequest")
        fun provideSignInRequest(
            app: Application
        ) = BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest
                    // 1.
                    .GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // 2.
                    .setServerClientId(app.getString(R.string.your_web_client_id))
                    // 3.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            // 4.
            .setAutoSelectEnabled(true)
            .build()

        @Provides
        @Named("signUpRequest")
        fun provideSignUpRequest(
            app: Application
        ) = BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest
                    // 1.
                    .GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // 2.
                    .setServerClientId(app.getString(R.string.your_web_client_id))
                    // 5.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        //
    }

    override suspend fun signUp(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }
}