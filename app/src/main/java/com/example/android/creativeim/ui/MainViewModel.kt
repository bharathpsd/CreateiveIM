package com.example.android.creativeim.ui

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.creativeim.data.User
import com.example.android.creativeim.messagedata.MessageData
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.utils.EventProgress
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

    private val _messages = MutableLiveData<List<MessageData>>()
    val messages: LiveData<List<MessageData>> = _messages

    private val _friends = MutableLiveData<List<User>>()
    val friends: LiveData<List<User>> = _friends

    private val _openUser = MutableLiveData<EventProgress<User>>()
    val openUser: LiveData<EventProgress<User>> = _openUser

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


    private fun updateEvent(user: Result<Any>) {
        Logger.log(TAG, "inside Update event with ${user.data}")
        _authEvent.value = user
        if (user.data is FirebaseUser) {
            currentUser.value = user.data
        }
    }

    override fun onSuccess(user: Result<Any>) {
        _auth.value = false
        Logger.log(TAG, "Received Firebase User :  ${user.data.toString()}" )
        when (user.data) {
            is FirebaseUser -> {
                currentUser.value = user.data
                updateEvent(user)
            }
            is DocumentReference -> {
                updateEvent(user)
            }
            is User -> {
                updateEvent(user)
            }
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

    fun signOutUser() {
        viewModelScope.launch {
            Logger.log(TAG, "User Signed Out method")
            repo.signOutUser()
        }
    }


    fun searchUser(userName: EditText) {
        Logger.log(TAG, "Inside Search User Method")
        if (userName.text.isNullOrEmpty()) {
            userName.error = "Username cannot be empty"
        } else if (currentUser.value!!.displayName.equals(userName.text.toString())) {
            userName.error = "Grow up. This is your id"
        } else {
            _auth.value = true
            viewModelScope.launch {
                Logger.log(TAG, "Searching user...")
                searchUserWithUserId(
                    currentUser.value!!.uid,
                    currentUser.value!!.displayName.toString(),
                    userName.text.toString()
                )
            }
        }
    }

    private suspend fun searchUserWithUserId(
        currentUserId: String,
        currentUserName: String,
        text: String
    ) {
        repo.searchUid(currentUserId, currentUserName, text, this)
    }

    fun sendMessage(
        message: String,
        fromId: String,
        toId: String,
        timeStamp: Long,
        toUser: String,
        fromUser: String
    ) {
        viewModelScope.launch {
            repo.sendMessage(message, fromId, toId, timeStamp, toUser, fromUser)
        }
    }

    fun getMessages(fromId: String, toId: String) {
        Logger.log(TAG, "Inside getMessages")
        viewModelScope.launch {
            getMessagesFromDatabase(_messages, fromId, toId)
        }
    }

    private suspend fun getMessagesFromDatabase(
        message: MutableLiveData<List<MessageData>>,
        fromId: String,
        toId: String
    ) {
        Logger.log(TAG, "Inside getMessages Database")
        repo.getMessages(message, fromId, toId)
    }

    fun addUserToFriendsList(currentUserId: String, currentUserName: String, user: User) {
        viewModelScope.launch {
            repo.addUserFriends(currentUserId, currentUserName, user)
        }
    }

    fun getFriendsForUser(uid: String, username: String) {
        viewModelScope.launch {
            getFriendsList(uid, username)
        }
    }

    private suspend fun getFriendsList(uid: String, username: String) {
        repo.getUserFriends(_friends, uid, username, this)
    }

    fun openUser(user: User) {
        _openUser.value = EventProgress(user)
    }

}