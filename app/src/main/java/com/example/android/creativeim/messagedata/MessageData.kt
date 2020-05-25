package com.example.android.creativeim.messagedata

import java.io.Serializable

data class MessageData(
    val message: String,
    val fromId: String,
    val toId: String?,
    val timeStamp: Long,
    val toUserName: String,
    val fromUserName: String
) : Serializable