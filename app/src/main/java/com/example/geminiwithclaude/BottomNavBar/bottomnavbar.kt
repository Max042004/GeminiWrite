package com.example.geminiwithclaude.BottomNavBar

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
//import com.example.geminiwithclaude.Activities.CreateFlashcardsActivity
//import com.example.geminiwithclaude.Activities.ReadArticlesActivity
//import com.example.geminiwithclaude.Activities.WriteArticleActivity
import com.example.geminiwithclaude.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/*sealed class BottomNavItem(val itemId: Int, val label: String, val icon: Int) {
    object Write : BottomNavItem(R.id.navigation_write, "Write", R.drawable.ic_home_black)
    object Cards : BottomNavItem(R.id.navigation_cards, "Cards", R.drawable.ic_notifications_black)
    object Articles : BottomNavItem(R.id.navigation_articles, "Articles", R.drawable.ic_dashboard_black)
}
@Composable
fun BottomNavigationBar(
    activity: ComponentActivity,
    currentDestination: BottomNavItem
) {
    AndroidView(
        factory = { context ->
            BottomNavigationView(context).apply {
                inflateMenu(R.menu.bottom_nav_menu)
                selectedItemId = currentDestination.itemId
                setOnItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.navigation_write -> {
                            activity.startActivity(Intent(activity, WriteArticleActivity::class.java))
                            true
                        }
                        R.id.navigation_cards -> {
                            activity.startActivity(Intent(activity, CreateFlashcardsActivity::class.java))
                            true
                        }
                        R.id.navigation_articles -> {
                            activity.startActivity(Intent(activity, ReadArticlesActivity::class.java))
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    )
}*/