package com.example.android.creativeim.repo

import com.example.android.creativeim.apiService.InitRetro
import com.example.android.creativeim.messagedata.NotificationData
import com.example.android.creativeim.utils.Logger

class SendMessageService : InitRetro(), SendMessageServiceInterface {

    override suspend fun sendMessage(data: NotificationData) = try {
        val response = api.postMessage(data)
        Logger.log("SendMessageService", response.toString())
    } catch (e: Exception) {
        Logger.log("SendMessageService : ", e.message.toString())
    }

}