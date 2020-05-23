package com.example.android.creativeim.repo

import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result
import com.google.firebase.auth.FirebaseUser

interface LoginRepoInterface {

    suspend fun loginWithEmailandPwd(userId : String, pwd : String, authCompleteListener: OnAuthCompleteListener)

    suspend fun createUserAuth(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener)

    suspend fun getUser() : Result<FirebaseUser>

}