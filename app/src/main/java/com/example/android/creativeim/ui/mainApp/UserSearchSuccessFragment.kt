package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.creativeim.MainActivity
import com.example.android.creativeim.R
import com.example.android.creativeim.data.User
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_user_search_success.*

private const val TAG = "UserSearchSuccessFragment"

class UserSearchSuccessFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_search_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        setUpClickListener()
    }

    private fun setViewListeners() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    if (it.data is FirebaseUser) {
                        Logger.log(TAG, "Inside Success of user data")
                        currentUser = it.data
                    }
                }

                is Result.Error -> showSnackBar(it.message.toString())
            }
        })
    }

    private fun setUpClickListener() {
        arguments?.let {
            val user = it.get("user") as User
            Logger.log(TAG, "$user")
            val text = "Do you want to add ${user.firstName} ${user.lastName} as your friend ?"
            userSearchData.text = text
        }

        yes.setOnClickListener {
//            showSnackBar("Successfully added user into your chat list")
            viewModel.addUserToFriendsList(
                currentUser.uid,
                currentUser.displayName.toString(),
                arguments.let { it!!.get("user") as User })
            navigateToMessagesFragment()
        }

        no.setOnClickListener {
            showSnackBar("User is not added to your chat list")
            navigateToHomeScreen()
        }
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(UserSearchSuccessFragmentDirections.actionUserSearchSuccessFragmentToHomeFragment())
    }

    private fun navigateToMessagesFragment() {
        val action =
            UserSearchSuccessFragmentDirections.actionUserSearchSuccessFragmentToMessagesFragment(
                requireArguments().get("user") as User
            )
        findNavController().navigate(action)
    }

    private fun showSnackBar(message: String) {
        (requireContext() as MainActivity).hideKeyBoard(requireView())
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).setAction(R.string.ok) {
        }.show()
    }

}
