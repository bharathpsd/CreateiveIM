package com.example.android.creativeim.utils

import com.example.android.creativeim.repo.LoginRepo
import com.example.android.creativeim.repo.LoginRepoInterface
import com.google.firebase.auth.FirebaseAuth

object ServiceLocator {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getLoginRepo() : LoginRepoInterface {
        return LoginRepo(getFirebaseAuth())
    }

}