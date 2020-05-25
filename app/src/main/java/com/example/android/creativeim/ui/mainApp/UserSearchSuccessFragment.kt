package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.creativeim.R
import com.example.android.creativeim.User
import com.example.android.creativeim.utils.Logger
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_search_success.*

private const val TAG = "UserSearchSuccessFragment"

class UserSearchSuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_search_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpClickListener()
    }

    private fun setUpClickListener() {

        arguments?.let {
            val user = it.get("user") as User
            Logger.log(TAG, "$user")
            val text = "Do you want to add ${user.firstName} ${user.lastName} as your friend ?"
            userSearchData.text = text
        }

        yes.setOnClickListener {
            showSnackBar("Successfully added user into your chat list")
        }

        no.setOnClickListener {
            showSnackBar("User is not added to your chat list")
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).setAction(R.string.ok) {
        }.show()
    }

}
