package com.example.android.creativeim.utils

interface OnAuthCompleteListener {
        fun onSuccess(user: Result<Any>)
        fun onFailure(message : Result<String>)
}