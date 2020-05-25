package com.example.android.creativeim.repo

import androidx.lifecycle.MutableLiveData
import com.example.android.creativeim.data.User
import com.example.android.creativeim.messagedata.MessageData
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
import com.google.firebase.firestore.*

private const val TAG = "LoginRepo"

class LoginRepo (
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val sendMessageService: SendMessageServiceInterface
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
                            Logger.log(TAG, "Task Result ID: " + taskRef.result?.id)
                            val doc = taskRef.result
                            if (doc?.id != null || doc?.id!!.isNotEmpty()) {
                                authCompleteListener.onSuccess(Success(doc))
                            }
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

    override suspend fun signOutUser() {
        Logger.log(TAG, "Signing out user")
        firebaseAuth.signOut()
    }

    override suspend fun deleteAccount() {

    }

    override suspend fun sendMessage(
        message: String,
        fromId: String,
        toId: String,
        timeStamp: Long,
        toUser: String,
        fromUser: String
    ) {
        val reference = fireStore.collection("user-messages/$fromId/$toId")
        val reverseRef = fireStore.collection("user-messages/$toId/$fromId")
        val messageData = MessageData(message, fromId, toId, timeStamp, toUser, fromUser)
        reference.add(messageData)
        reverseRef.add(messageData)
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

    private fun searchUserWithId(userId: String): Task<QuerySnapshot> {
        return collectionPrefs
            .whereEqualTo("userName", userId)
            .get()
    }

    override suspend fun searchUid(userId: String, authCompleteListener: OnAuthCompleteListener) {
        Logger.log(TAG, "authcompleteLister : $authCompleteListener")
        searchUserWithId(userId)
            .addOnCompleteListener {
                searchUser(it, authCompleteListener)
            }
    }

    override suspend fun getMessages(
        messages: MutableLiveData<List<MessageData>>,
        fromId: String,
        toId: String
    ) {
        Logger.log(TAG, "Inside getMessages of LoginRepo")
        val messagesList = arrayListOf<MessageData>()
        val reference = fireStore.collection("user-messages/$fromId/$toId")
        reference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Logger.log(TAG, it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (document in it) {
                    Logger.log(TAG, "Document ID : ${document.id}")
                    val messageData = dataToDocument(document)
                    messagesList.add(messageData)
                }
                messages.value = messagesList
                return@addSnapshotListener
            }
            Logger.log(TAG, "Exception getting messages")
        }
    }

    override suspend fun addUserFriends(
        currentUserId: String,
        currentUserName: String,
        user: User
    ) {
        val reference = fireStore.collection("friends/$currentUserId/$currentUserName")
        reference.add(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.log(TAG, "Added Successfully")
                } else if (!it.isSuccessful || it.exception.toString().isNotEmpty()) {
                    Logger.log(TAG, "Adding Friends Failed")
                }
            }
    }

    override suspend fun getUserFriends(
        friends: MutableLiveData<List<User>>,
        currentUserId: String,
        username: String,
        authCompleteListener: OnAuthCompleteListener
    ) {
        val reference = fireStore.collection("friends/$currentUserId/$username")
        Logger.log(TAG, "Inside get user Friends ")
        val usersList = arrayListOf<User>()
        reference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Logger.log(TAG, it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                Logger.log(TAG, "Query Success")
                for (document in it) {
                    Logger.log(TAG, "Document ID : ${document.id}")
                    val userData = dataToObject(document)
                    usersList.add(userData)
                }
                friends.value = usersList
                return@addSnapshotListener
            }
            Logger.log(TAG, "Exception getting messages")
        }
    }

    private fun searchUser(it: Task<QuerySnapshot>, authCompleteListener: OnAuthCompleteListener) {
        Logger.log(TAG, "Person Pref search is complete : $it")
        it.result?.let { query ->
            if (query.documents.isNotEmpty()) {
                for (document in query) {
                    Logger.log(TAG, "QueryDocumentSnapshot ID : ${document.id}")
                    collectionPrefs.document(document.id).get()
                        .addOnCompleteListener { result ->
                            try {
                                if (!result.isSuccessful) {
                                    Logger.log(TAG, "No Person with this name")
                                    authCompleteListener.onFailure(Error(result.exception!!.message.toString()))
                                } else if (result.isSuccessful) {
                                    Logger.log(TAG, "Result is successful : ${result.result}")
                                    result.result.let { user ->
                                        Logger.log(TAG, "The user is : $user")
                                        dataToObject(user, authCompleteListener)
                                        return@addOnCompleteListener
                                    }
                                } else if (result.exception.toString().isNotEmpty()) {
                                    Logger.log(TAG, "No Person with this name: ${result.exception}")
                                    authCompleteListener.onFailure(Error(result.exception!!.message.toString()))
                                    return@addOnCompleteListener
                                }
                            } catch (e: Exception) {
                                Logger.log(
                                    TAG,
                                    " Caught Exception during search : ${result.exception}"
                                )
                                authCompleteListener.onFailure(Error(e.message.toString()))
                                return@addOnCompleteListener
                            }
                        }
                }
            } else {
                authCompleteListener.onFailure(Error(it.result.toString()))
                Logger.log(TAG, "No Person with this name")
                return
            }
        }
//        authCompleteListener.onFailure(Error(it.exception!!.message.toString()))
        Logger.log(TAG, "No Person with this name : ${it.exception}")
        return
    }

    private fun dataToObject(
        user: DocumentSnapshot?,
        authCompleteListener: OnAuthCompleteListener
    ) {
        val firstName = user!!.get("firstName").toString()
        val lastName = user.get("lastName").toString()
        val userId = user.get("userId").toString()
        val age = user.get("age").toString().toInt()
        val userName = user.get("userName").toString()
        val person = User(
            userId,
            userName,
            firstName,
            lastName,
            age
        )
        Logger.log(TAG, "Person : $person")
        authCompleteListener.onSuccess(Success(person))
        return
    }

    private fun dataToObject(
        user: DocumentSnapshot?
    ): User {
        val firstName = user!!.get("firstName").toString()
        val lastName = user.get("lastName").toString()
        val userId = user.get("userId").toString()
        val age = user.get("age").toString().toInt()
        val userName = user.get("userName").toString()
        val person = User(
            userId,
            userName,
            firstName,
            lastName,
            age
        )
        Logger.log(TAG, "Person : $person")
        return person
    }


    private fun dataToDocument(
        document: QueryDocumentSnapshot?
    ): MessageData {
        val message = document!!.get("message").toString()
        val fromId = document.get("fromId").toString()
        val toId = document.get("toId").toString()
        val timeStamp = document.get("timeStamp").toString().toLong()
        val toUserName = document.get("toUserName").toString()
        val fromUserName = document.get("fromUserName").toString()
        val messageData = MessageData(
            message,
            fromId,
            toId,
            timeStamp,
            toUserName,
            fromUserName
        )
        Logger.log(TAG, "Person : $messageData")
        return messageData
    }

}