package com.example.android.creativeim.utils

import com.example.android.creativeim.constants.Constants.MY_TOPIC
import com.example.android.creativeim.repo.LoginRepo
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.repo.SendMessageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

object ServiceLocator {

    private fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    private fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    private fun getMessagingService() = SendMessageService()


    fun getAuthRepo(): LoginRepoInterface {
        FirebaseMessaging.getInstance().subscribeToTopic(MY_TOPIC)
        return LoginRepo(getFirebaseAuth(), getFireStore(), getMessagingService())
    }

}