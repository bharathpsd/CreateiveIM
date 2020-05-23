package com.example.android.creativeim.utils

import com.google.firebase.auth.FirebaseUser

interface OnAuthCompleteListener {
        fun onSuccess(user : Result<FirebaseUser>)
        fun onFailure(message : Result<String>)
}