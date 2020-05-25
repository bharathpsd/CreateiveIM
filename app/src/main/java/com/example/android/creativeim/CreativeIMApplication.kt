package com.example.android.creativeim

import android.app.Application
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.utils.ServiceLocator

class CreativeIMApplication : Application() {

    lateinit var loginRepo : LoginRepoInterface

    override fun onCreate() {
        super.onCreate()
        loginRepo = ServiceLocator.getAuthRepo()
    }

}