package com.example.android.creativeim.ui

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.creativeim.User
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.Result.Success
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel (
    private val repo : LoginRepoInterface
) : ViewModel(), OnAuthCompleteListener {

    private val _auth = MutableLiveData(false)
    val auth : LiveData<Boolean> = _auth

    private val _authUser : MutableLiveData<FirebaseUser>? =  null
    val authUser : LiveData<FirebaseUser>? = _authUser

    private val _authEvent = MutableLiveData<Result<Any>>()
    val authEvent = _authEvent

    private val currentUser = MutableLiveData<FirebaseUser>()

    init {
        getCurrentUser()
    }

    fun validateUNandPassword(userNameView : EditText, pwdNameView : EditText, screen : Int){

        if(userNameView.text!!.isEmpty()) {
            Logger.log(TAG, "Username Field is Empty")
            userNameView.error = "Username Field is Empty"
        }

        if(pwdNameView.text!!.isEmpty()) {
            Logger.log(TAG, "Password Field is Empty")
            pwdNameView.error = "Password Field is Empty"
        }

        if(userNameView.text!!.isNotEmpty() && pwdNameView.text!!.isNotEmpty()) {
            _auth.value = true
            viewModelScope.launch {
                when(screen) {
                    1 -> registerUser(userNameView.text.toString(), pwdNameView.text.toString())
                    2 -> loginUser(userNameView.text.toString(), pwdNameView.text.toString())
                    else -> registerUser(userNameView.text.toString(), pwdNameView.text.toString())
                }
            }
        }
    }

    private suspend fun registerUser(userId : String, pwd : String) {
        repo.createUserAuth(userId, pwd, this)
    }

    private suspend fun loginUser(userId : String, pwd : String) {
        repo.loginWithEmailandPwd(userId, pwd, this)
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
           repo.getUser().data?.let {user ->
               updateEvent(Success(user))
           }
        }
    }

    override fun onSuccess(user: Result<Any>) {
        _auth.value = false
        Logger.log(TAG, "Received Firebase User :  ${user.data.toString()}" )
        if (user.data is FirebaseUser) {
            currentUser.value = user.data
            updateEvent(user)
        } else if (user.data is DocumentReference) {
            updateEvent(user)
        }
    }

    private fun updateEvent(user: Result<Any>) {
        _authEvent.value = user
        if (user.data is FirebaseUser) {
            currentUser.value = user.data
        }
    }

    override fun onFailure(message: Result<String>) {
        _auth.value = false
        Logger.log(TAG, "Received Firebase Error : $message")
        updateEvent(message)
    }

    fun updateUserDetails(
        userName: EditText,
        firstName: EditText,
        lastName: EditText,
        age: EditText
    ) {
        if (userName.text!!.isEmpty()) {
            Logger.log(TAG, "Username Field is Empty")
            userName.error = "Username Field is Empty"
        }

        if (firstName.text!!.isEmpty()) {
            Logger.log(TAG, "First Name Field is Empty")
            firstName.error = "First Name Field is Empty"
        }

        if (lastName.text!!.isEmpty()) {
            Logger.log(TAG, "Last Name Field is Empty")
            lastName.error = "Last Name Field is Empty"
        }

        if (lastName.text.isNotEmpty() || firstName.text.isNotEmpty() || userName.text.isNotEmpty()) {
            _auth.value = true
            viewModelScope.launch {
                val user = User(
                    currentUser.value!!.uid,
                    userName.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    age.text.toString().toInt()
                )
                updateUser(user)
            }
        }
    }

    private suspend fun updateUser(user: User) {
        Logger.log(TAG, "Inside update user details")
        repo.updateUserDetails(user, this)
    }

}