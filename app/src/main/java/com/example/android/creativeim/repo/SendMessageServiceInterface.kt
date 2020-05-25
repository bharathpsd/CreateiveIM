package com.example.android.creativeim.repo

import com.example.android.creativeim.messagedata.NotificationData

interface SendMessageServiceInterface {

    suspend fun sendMessage(data: NotificationData)

}