package com.example.android.creativeim.repo

import com.example.android.creativeim.User
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.Result.Error
import com.example.android.creativeim.utils.Result.Success
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "LoginRepo"

class LoginRepo (
    private val firebaseAuth: FirebaseAuth,
    fireStore: FirebaseFirestore
) : LoginRepoInterface{

    private val collectionPrefs: CollectionReference = fireStore.collection("persons")

    override suspend fun loginWithEmailandPwd(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(userId, pwd)
            .addOnCompleteListener {
                handleAuthResult(it, authCompleteListener)
            }
    }

    override suspend fun createUserAuth(userId: String, pwd: String, authCompleteListener: OnAuthCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(userId, pwd)
            .addOnCompleteListener {
                handleAuthResult(it, authCompleteListener)
            }
    }

    override suspend fun getUser() : Result<FirebaseUser> {
        firebaseAuth.currentUser?.let {
            return Success(it)
        }
        return Error("No User")
    }

    override suspend fun updateUserDetails(
        user: User,
        authCompleteListener: OnAuthCompleteListener
    ) {
        try {
            firebaseAuth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().apply {
                displayName = user.userName
            }.build())!!.addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.log(TAG, "display username update successful")
                    collectionPrefs.add(user).addOnCompleteListener { taskRef ->
                        Logger.log(TAG, taskRef.toString())
                        if (taskRef.isSuccessful) {
                            Logger.log(TAG, "User details update successful")
                            authCompleteListener.onSuccess(Success(collectionPrefs.document()))
                        } else {
                            Logger.log(TAG, "User details update failed")
                            authCompleteListener.onFailure(Error(taskRef.result.toString()))
                        }
                    }
                } else {
                    Logger.log(TAG, "display username update failed")
                    authCompleteListener.onFailure(Error(it.result.toString()))
                }
            }
        } catch (e: Exception) {
            authCompleteListener.onFailure(Error(e.message.toString()))
        }

    }

    private fun handleAuthResult(
        it: Task<AuthResult>,
        authCompleteListener: OnAuthCompleteListener
    ) {
        try {
            if (!it.isSuccessful) {
                Logger.log(TAG, "User not created with error : " + it.result)
                authCompleteListener.onFailure(Error(it.result.toString()))
            } else {
                authCompleteListener.onSuccess(Success(firebaseAuth.currentUser!!))
            }
        } catch (e: Exception) {
            authCompleteListener.onFailure(Error(it.exception!!.message.toString()))
        }
    }


}