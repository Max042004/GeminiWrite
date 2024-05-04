package com.example.geminiwithclaude.Screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Scaffold
import com.example.geminiwithclaude.BottomNavBar.BottomNavItem
import com.example.geminiwithclaude.BottomNavBar.BottomNavigationBar

@Composable
fun ScaffoldExample() {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
            activity = this@WriteArticleActivity,
            currentDestination = BottomNavItem.Write
        )
        },
        content = {},

    )
}