package com.example.android.creativeim

import android.app.Application
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.utils.ServiceLocator
import com.google.firebase.FirebaseApp

class CreativeIMApplication : Application() {

    lateinit var loginRepo : LoginRepoInterface

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        loginRepo = ServiceLocator.getAuthrepo(this)
    }

}