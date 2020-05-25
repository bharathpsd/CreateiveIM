package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.creativeim.R
import com.example.android.creativeim.data.User
import com.example.android.creativeim.databinding.FragmentHomeBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.ui.UserAdpater
import com.example.android.creativeim.utils.EventObserver
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.firebase.auth.FirebaseUser

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var viewBinding : FragmentHomeBinding

    private lateinit var listAdapter: UserAdpater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewmodel = viewModel
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setUpView()
        setUpAdapter()
        setHasOptionsMenu(true)
    }

    private fun setUpAdapter() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.viewmodel?.let {
            listAdapter = UserAdpater(viewModel)
            viewBinding.friends.layoutManager = layoutManager
            viewBinding.friends.adapter = listAdapter
        }
    }

    private fun setUpView() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    if (it.data is FirebaseUser)
                        setTextViews(it.data)
                }
                is Result.Error -> navigateToSplash()
            }
        })

        viewModel.openUser.observe(viewLifecycleOwner, EventObserver {
            navigateToMessagesFragment(it)
        })
    }

    private fun navigateToMessagesFragment(user: User) {
        val action = HomeFragmentDirections.actionHomeFragmentToMessagesFragment(user)
        findNavController().navigate(action)
    }

    private fun navigateToSplash() {
        Logger.log(TAG, "Navigating back to Splash Fragment")
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSplashFragment())
    }

    private fun setTextViews(user: FirebaseUser?) {
        user?.let {
            if (user.displayName.isNullOrEmpty()) {
                navigateToUserDetailsFragment()
            } else {
                val textToDisplay = "Logged in as ${user.displayName.toString()}"
                Logger.log(TAG, textToDisplay)
//                user_data.text = textToDisplay
                Logger.log(TAG, "Calling getMessages()")
                viewModel.getFriendsForUser(user.uid, user.displayName.toString())
            }
        }

    }

    private fun navigateToUserDetailsFragment() {
        Logger.log(TAG, "Navigating to User Details Fragment")
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserDetailsFragment())
    }

    private fun navigateToSearchUserFragment() {
        Logger.log(TAG, "Navigating to SearchUserFragment")
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchUserFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                viewModel.signOutUser()
                navigateToSplash()
            }
            R.id.new_message -> navigateToSearchUserFragment()
        }
        return super.onOptionsItemSelected(item)
    }

}
