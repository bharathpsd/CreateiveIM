package com.example.android.creativeim.repo

import com.example.android.creativeim.apiService.InitRetro
import com.example.android.creativeim.messagedata.NotificationData

class SendMessageService : InitRetro() {

    suspend fun sendMessage(data: NotificationData) {
        api.postMessage(data)
    }

}