package com.example.android.creativeim.apiService

import com.example.android.creativeim.constants.Constants
import com.example.android.creativeim.messagedata.MessageData
import com.example.android.creativeim.messagedata.NotificationData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationMessage {

    @POST("fcm/send")
    @Headers(
        "Authorization : key=${Constants.serverKey}",
        "Content-Type : ${Constants.contentType}"
    )
    suspend fun postMessage(
        @Body notificationData: NotificationData
    ): Response<MessageData>

}