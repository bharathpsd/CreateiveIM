package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.creativeim.R
import com.example.android.creativeim.databinding.FragmentUserDetailsBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference

private const val TAG = "UserDetailsFragment"

class UserDetailsFragment : Fragment() {

    private lateinit var viewBinding: FragmentUserDetailsBinding

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewmodel = viewModel
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setViewListeners()
    }

    private fun setViewListeners() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    if (it.data is DocumentReference) {
                        navigateToHomeFragment()
                    }
                }
                is Result.Error -> showSnackBar(it.message!!)
            }
        })
    }

    private fun navigateToHomeFragment() {
        Logger.log(TAG, "Navigating to Home Fragment")
        findNavController().navigate(UserDetailsFragmentDirections.actionUserDetailsFragmentToHomeFragment())
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).setAction(R.string.ok) {}.show()
    }

}
