package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.creativeim.R
import com.example.android.creativeim.databinding.FragmentHomeBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var viewBinding : FragmentHomeBinding

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
        setHasOptionsMenu(true)
    }

    private fun setUpView() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> setTextViews(it.data as FirebaseUser)
                is Result.Error -> navigateToSplash()
            }
        })
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
                user_data.text = textToDisplay
            }
            return
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
