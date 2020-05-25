package com.example.android.creativeim

import java.io.Serializable

data class User(
    val userId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val age: Int
) : Serializable