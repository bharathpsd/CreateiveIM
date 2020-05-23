package com.example.android.creativeim.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.creativeim.R
import com.example.android.creativeim.databinding.FragmentLoginBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var viewBinding : FragmentLoginBinding

    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewmodel = viewModel
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setEventListeners()
    }


    private fun setEventListeners() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Success -> navigateToHomeFragment()
                is Result.Error -> showToast(it.message!!)
            }
        })
    }

    private fun showToast(message: String) {
        Logger.log(TAG, "Error during registration with message : $message")
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).setAction(R.string.ok) {
        }.show()
    }

    private fun navigateToHomeFragment() {
        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

}
