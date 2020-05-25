package com.example.android.creativeim

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.android.creativeim.constants.Constants

object SharedPrefsManager {

    private fun getSharedPrefs(context: Context) =
        context.getSharedPreferences(Constants.SHARED_PREF_KEY, MODE_PRIVATE)

    fun saveStringPreference(context: Context, key: String, value: String) {
        getSharedPrefs(context).edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun loadStringPreference(context: Context, key: String): String {
        return getSharedPrefs(context).getString(key, "")!!
    }

}