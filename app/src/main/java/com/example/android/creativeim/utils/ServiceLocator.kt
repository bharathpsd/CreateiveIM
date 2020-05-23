package com.example.android.creativeim.utils

import com.example.android.creativeim.repo.LoginRepo
import com.example.android.creativeim.repo.LoginRepoInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ServiceLocator {

    private fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    private fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun getAuthRepo(): LoginRepoInterface {
        return LoginRepo(getFirebaseAuth(), getFireStore())
    }

}