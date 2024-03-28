package com.example.geminiwithclaude

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.viewModels

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
        viewModel.startListening()
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


