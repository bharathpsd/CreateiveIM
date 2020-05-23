package com.example.android.creativeim.utils

import android.content.Context
import com.example.android.creativeim.repo.LoginRepo
import com.example.android.creativeim.repo.LoginRepoInterface
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

object ServiceLocator {

    private fun getFirebaseAuth(context: Context) : FirebaseAuth {
        FirebaseApp.initializeApp(context.applicationContext)
        return FirebaseAuth.getInstance()
    }

    fun getAuthrepo(context : Context): LoginRepoInterface {
        return LoginRepo(getFirebaseAuth(context))
    }

}