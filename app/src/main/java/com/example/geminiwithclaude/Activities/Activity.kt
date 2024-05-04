package com.example.geminiwithclaude.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.example.geminiwithclaude.WriterApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiwithClaudeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WriterApp()
                }
            }
        }
        //viewModel.fetchAndListenForArticleData()
        //viewModel.startListening()
        //setContentView(R.layout.activity_main)
        //val tv_test = findViewById(R.id.tv_test)

    }

    override fun onStop() {
        super.onStop()
        //viewModel.stopListening()
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