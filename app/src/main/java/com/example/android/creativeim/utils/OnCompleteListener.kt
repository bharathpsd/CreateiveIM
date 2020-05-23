package com.example.android.creativeim.utils

import com.google.firebase.auth.FirebaseUser

interface OnAuthCompleteListener {
        fun onSuccess(user : Result.Success<FirebaseUser>)
        fun onFailure(message : Result.Error<String>)
}