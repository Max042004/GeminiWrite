package com.example.geminiwithclaude.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.geminiwithclaude.BottomNavBar.BottomNavItem
import com.example.geminiwithclaude.BottomNavBar.BottomNavigationBar
import com.example.geminiwithclaude.R
import com.example.geminiwithclaude.ui.theme.GeminiwithClaudeTheme
import com.google.android.material.navigation.NavigationBarView

class CreateFlashcardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiwithClaudeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                activity = this@WriteArticleActivity,
                                currentDestination = BottomNavItem.Write
                            )
                        }
                    CreateFlashcardsScreen()
                }
            }
        }
    }
}