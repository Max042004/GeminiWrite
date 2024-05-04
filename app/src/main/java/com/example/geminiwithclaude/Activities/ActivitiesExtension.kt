package com.example.geminiwithclaude.Activities

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import com.example.geminiwithclaude.R
import com.google.android.material.navigation.NavigationBarView

fun NavigationBarView.startActivityWithNavBarSharedTransition(
    activity:Activity,
    intent: Intent
) {
    val options = ActivityOptions.makeSceneTransitionAnimation(
        activity,
        this,
        activity.getString(R.string.nav_view_shared_element_transition_name)
    ).toBundle()
    activity.startActivity(intent, options)
}