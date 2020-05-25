package com.example.android.creativeim.repo

import com.example.android.creativeim.User
import com.example.android.creativeim.messagedata.NotificationData
import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result
import com.google.firebase.auth.FirebaseUser

interface LoginRepoInterface {

    suspend fun loginWithEmailandPwd(userId : String, pwd : String, authCompleteListener: OnAuthCompleteListener)

    suspend fun createUserAuth(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener)

    suspend fun getUser() : Result<FirebaseUser>

    suspend fun updateUserDetails(user: User, authCompleteListener: OnAuthCompleteListener)

    suspend fun signOutUser()

    suspend fun deleteAccount()

    suspend fun sendMessage(data: NotificationData)

    suspend fun searchUid(userId: String, authCompleteListener: OnAuthCompleteListener)

}