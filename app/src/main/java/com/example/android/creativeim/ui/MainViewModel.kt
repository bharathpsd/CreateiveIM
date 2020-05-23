package com.example.android.creativeim.ui

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.OnAuthCompleteListener
import com.example.android.creativeim.utils.Result.Error
import com.example.android.creativeim.utils.Result.Success
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel (
    private val repo : LoginRepoInterface
) : ViewModel(), OnAuthCompleteListener {

    private val _auth = MutableLiveData(false)
    val auth : LiveData<Boolean> = _auth

    private val _authUser : MutableLiveData<FirebaseUser>? =  null
    val authUser : LiveData<FirebaseUser>? = _authUser

    fun validateUNandPassword(userNameView : EditText, pwdNameView : EditText, screen : Int){

        if(userNameView.text!!.isEmpty()) {
            Logger.log(TAG, "Username Field is Empty")
        }

        if(pwdNameView.text!!.isEmpty()) {
            Logger.log(TAG, "Password Field is Empty")
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

    fun getCurrentUser() {
        viewModelScope.launch {
           repo.getUser().data?.let {
               _authUser!!.value = it
           }
        }
    }

    override fun onSuccess(user : Success<FirebaseUser>)  {
        _auth.value = false
        Logger.log(TAG, "Received Firebase User :  ${user.data.toString()}" )
//        updateEvent(user)
    }

    override fun onFailure(message : Error<String>)  {
        _auth.value = false
        Logger.log(TAG, "Received Firebase Error : $message")
//        updateEvent(message)
    }


}