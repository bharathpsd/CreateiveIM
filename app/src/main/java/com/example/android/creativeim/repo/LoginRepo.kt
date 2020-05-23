package com.example.android.creativeim.repo

import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.Result.Error
import com.example.android.creativeim.utils.Result.Success
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "LoginRepo"

class LoginRepo (
    private val firebaseAuth : FirebaseAuth
) : LoginRepoInterface{
    override suspend fun loginWithEmailandPwd(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(userId, pwd)
            .addOnCompleteListener {
                if(it.isComplete && it.exception.toString().isNotEmpty()) {
                    authCompleteListener.onFailure(Error(it.exception!!.message.toString()))
                } else if (!it.isSuccessful) {
                    Logger.log(TAG, "User not created with error : " + it.result)
                    authCompleteListener.onFailure(Error(it.result.toString()))
                } else {
                    authCompleteListener.onSuccess(Success(firebaseAuth.currentUser!!))
                }
            }
    }

    override suspend fun createUserAuth(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(userId, pwd)
            .addOnCompleteListener {
                if(it.isComplete && it.exception.toString().isNotEmpty()) {
                    authCompleteListener.onFailure(Error(it.exception!!.message.toString()))
                } else if (!it.isSuccessful) {
                    Logger.log(TAG, "User not created with error : " + it.result)
                    authCompleteListener.onFailure(Error(it.result.toString()))
                } else {
                    authCompleteListener.onSuccess(Success(firebaseAuth.currentUser!!))
                }
            }
    }

    override suspend fun getUser() : Result<FirebaseUser> {
        firebaseAuth.currentUser?.let {
            return Success(it)
        }
        return Error("No User")
    }

}