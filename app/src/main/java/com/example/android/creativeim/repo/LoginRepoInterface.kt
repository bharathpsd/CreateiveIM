package com.example.android.creativeim.repo

import androidx.lifecycle.MutableLiveData
import com.example.android.creativeim.data.User
import com.example.android.creativeim.messagedata.MessageData
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

    suspend fun sendMessage(
        message: String,
        fromId: String,
        toId: String,
        timeStamp: Long,
        toUser: String,
        fromUser: String
    )

    suspend fun searchUid(userId: String, authCompleteListener: OnAuthCompleteListener)

    suspend fun getMessages(
        messages: MutableLiveData<List<MessageData>>,
        fromId: String,
        toId: String
    )

    suspend fun addUserFriends(currentUserId: String, currentUserName: String, user: User)

    suspend fun getUserFriends(
        friends: MutableLiveData<List<User>>,
        currentUserId: String,
        username: String,
        authCompleteListener: OnAuthCompleteListener
    )

}