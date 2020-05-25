package com.example.android.creativeim.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val age: Int
) : Parcelable