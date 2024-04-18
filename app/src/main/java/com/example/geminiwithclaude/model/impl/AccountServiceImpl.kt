package com.example.geminiwithclaude.model.impl

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
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
import com.example.geminiwithclaude.model.Constants
import com.example.geminiwithclaude.model.DataProvider
import com.example.geminiwithclaude.model.DeleteAccountResponse
import com.example.geminiwithclaude.model.FirebaseSignInResponse
import com.example.geminiwithclaude.model.OneTapSignInResponse
import com.example.geminiwithclaude.model.Response
import com.example.geminiwithclaude.model.SignOutResponse
import com.example.geminiwithclaude.model.isWithinPast
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import java.util.Date

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var googleSignInClient: GoogleSignInClient,

    @Named(Constants.SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(Constants.SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,) : AccountService {

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

    override suspend fun onTapSignIn(): OneTapSignInResponse {
        return try {
            // 1. Start the sign-in process by calling beginSignIn(beginSignInRequest:)
            // on the SignInClient(it is api) object and pass the signInRequest(also from api).
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                // 2. If the sign-in process failed because the user is not previously authorized,
                // then start the sign-up process by calling beginSignIn(beginSignInRequest:)
                // on the SignInClient object and pass the signUpRequest.
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch(e: Exception) {
                Response.Failure(e)
            }
        }
    }

    override suspend fun verifyGoogleSignIn(): Boolean {
        auth.currentUser?.let { user ->
            if (user.providerData.map { it.providerId }.contains("google.com")) {
                return try {
                    googleSignInClient.silentSignIn().await()
                    true
                } catch (e: ApiException) {
                    Log.e(TAG, "Error: ${e.message}")
                    signOut()
                    false
                }
            }
        }
        return false
    }

    override suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse {
        // 1. Create a AuthCredential using the googleIdToken from the given SignInCredential.
        val googleCredential = GoogleAuthProvider
            .getCredential(credential.googleIdToken, null)
        // 2. Then call the previously created authenticateUser(credentials:)
        // and pass the googleCredential we just defined to link or sign-in with Firebase.
        return authenticateUser(googleCredential)
    }

    private suspend fun authenticateUser(credential: AuthCredential): FirebaseSignInResponse {
        return if (auth.currentUser != null) {
            authLink(credential)
        } else {
            authSignIn(credential)
        }
    }

    private suspend fun authSignIn(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            Log.i(TAG, "User: ${authResult?.user?.uid}")
            DataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
    }

    private suspend fun authLink(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.currentUser?.linkWithCredential(credential)?.await()
            Log.i(TAG, "User: ${authResult?.user?.uid}")
            DataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: FirebaseAuthException) {
            when (error.errorCode) {
                Constants.AuthErrors.CREDENTIAL_ALREADY_IN_USE,
                Constants.AuthErrors.EMAIL_ALREADY_IN_USE -> {
                    Log.e(TAG, "FirebaseAuthError: authLink(credential:) failed, ${error.message}")
                    return authSignIn(credential)
                }
            }
            Response.Failure(error)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
    }

    override fun checkNeedsReAuth(): Boolean {
        auth.currentUser?.metadata?.lastSignInTimestamp?.let { lastSignInDate ->
            return !Date(lastSignInDate).isWithinPast(5)
        }
        return false
    }

    override suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun authorizeGoogleSignIn(): String? {
        auth.currentUser?.let { user ->
            if (user.providerData.map { it.providerId }.contains("google.com")) {
                try {
                    val account = googleSignInClient.silentSignIn().await()
                    return account.idToken
                } catch (e: ApiException) {
                    Log.e(TAG, "Error: ${e.message}")
                }
            }
        }
        return null
    }


    override suspend fun signUp(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            Response.Success(true)
        }
        catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    private suspend fun reauthenticate(googleIdToken: String) {
        val googleCredential = GoogleAuthProvider
            .getCredential(googleIdToken, null)
        auth.currentUser?.reauthenticate(googleCredential)?.await()
    }

    override suspend fun deleteUserAccount(googleIdToken: String?): DeleteAccountResponse {
        return try {
            auth.currentUser?.let { user ->
                if (user.providerData.map { it.providerId }.contains("google.com")) {
                    // Re-authenticate if needed
                    if (checkNeedsReAuth() && googleIdToken != null) {
                        reauthenticate(googleIdToken)
                    }
                    // Revoke
                    googleSignInClient.revokeAccess().await()
                    oneTapClient.signOut().await()
                }
                // Delete firebase user
                auth.currentUser?.delete()?.await()
                Response.Success(true)
            }
            Log.e(TAG, "FirebaseAuthError: Current user is not available")
            Response.Success(false)
        }
        catch (e: Exception) {
            Log.e(TAG, "FirebaseAuthError: Failed to delete user")
            Response.Failure(e)
        }
    }

    companion object {
        private const val TAG = "AccountService"
    }
}