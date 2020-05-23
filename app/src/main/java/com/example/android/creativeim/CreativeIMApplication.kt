package com.example.android.creativeim

import android.app.Application
import com.example.android.creativeim.utils.ServiceLocator

class CreativeIMApplication : Application() {

    val loginRepo = ServiceLocator.getLoginRepo()

}