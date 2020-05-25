package com.example.android.creativeim.apiService

import com.example.android.creativeim.constants.Constants
import com.example.android.creativeim.constants.Constants.contentType
import com.example.android.creativeim.messagedata.NotificationData
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationMessage {

    @POST("fcm/send")
    @Headers(
        "Authorization: key=${Constants.serverKey}",
        "Content-Type: $contentType"
    )
    suspend fun postMessage(
        @Body notificationData: NotificationData
    ): ResponseBody

}