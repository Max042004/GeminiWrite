package com.example.geminiwithclaude.Di

import android.app.Application
import android.content.Context
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.model.Constants
import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.impl.AccountServiceImpl
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AccountServiceImpl): AccountService = impl

    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    fun provideGoogleSignInOptions(
        app: Application,
    ) = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.your_web_client_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    @Named(Constants.SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                // 1. Use the .GoogleIdTokenRequestOptions() to enable and configure the Google ID token request
                // (required for apps that use Google Sign-in).
                .GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // 2. Use the .setServerClientId(serverClientId) to pass
                // your server’s Client ID, not your Android client ID, which we can get from the Firebase Console.
                .setServerClientId(app.getString(R.string.your_web_client_id))
                // 3. In signInRequest, only show the accounts that were previously used to sign in,
                // by passing true to .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts:)
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // 4. Enable automatically sign-in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(Constants.SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                // 1.
                .GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // 2. Your server's client ID, not your Android client ID.
                .setServerClientId(app.getString(R.string.your_web_client_id))
                // 5. In SignUpRequest, show all available accounts on the device by
                // passing false to .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts:),
                // this is the case that theh user does not have a previously authorized account.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()
}