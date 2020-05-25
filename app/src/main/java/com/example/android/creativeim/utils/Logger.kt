package com.example.android.creativeim.utils

import android.util.Log

object Logger {

    fun log(tag : String, message : String) {
        Log.e(tag, message)
    }

}