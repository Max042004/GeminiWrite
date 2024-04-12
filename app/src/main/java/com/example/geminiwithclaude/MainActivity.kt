package com.example.geminiwithclaude

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import com.example.geminiwithclaude.Viewmodel.EnglishWritingViewModel
import com.example.geminiwithclaude.ui.theme.GeminiWritingScreen
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    private val viewModel: EnglishWritingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiwithClaudeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeminiWritingScreen()

                }
            }
        }
        viewModel.fetchAndListenForArticleData()
        //viewModel.startListening()
        val authProvider: List<AuthUI.IdpConfig> = listOf(
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val authListener: FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
                val user: FirebaseUser? = auth.currentUser
                if (user == null) {
                    val intent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(authProvider)
                        .setAlwaysShowSignInMethodScreen(true)
                        .setIsSmartLockEnabled(false)
                        .build()
                    startActivityForResult(intent, this.RC_SIGN_IN)
                } else {
                    this.firebaseUser = user
                    displayInfo()
                }
            }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == this.RC_SIGN_IN) {
            if (resultCode != Activity.RESULT_OK) {
                val response = IdpResponse.fromResultIntent(data)
                Toast.makeText(applicationContext, response?.error?.errorCode.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun displayInfo() {
        val txtUserName = findViewById<TextView>(R.id.txtUserName)
        txtUserName.text = firebaseUser?.displayName
    }
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "已登出", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopListening()
        Log.d("Activity", "onStop called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Activity", "onPause called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity", "onDestroy called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Activity", "onResume called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Activity", "onRestart called")
    }
}


