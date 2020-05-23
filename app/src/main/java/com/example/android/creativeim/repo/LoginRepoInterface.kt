package com.example.android.creativeim.repo

interface LoginRepoInterface {

    suspend fun loginWithEmailandPwd(userId : String, pwd : String)

    suspend fun createUserAuth(userId: String, pwd: String)

    suspend fun getUser(userId: String)

}