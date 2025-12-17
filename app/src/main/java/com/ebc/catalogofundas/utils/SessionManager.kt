package com.ebc.catalogofundas.utils

import android.content.Context

object SessionManager {
    private const val PREFS_NAME = "user_session"
    private const val KEY_LOGGED_IN = "is_logged_in"

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun logout(context: Context) {
        setLoggedIn(context, false)
    }
}
