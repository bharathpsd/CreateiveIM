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
import com.example.android.creativeim.User
import com.example.android.creativeim.databinding.FragmentSearchUserBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.android.material.snackbar.Snackbar

private const val TAG = "SearchUserFragment"

class SearchUserFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var viewBinding: FragmentSearchUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSearchUserBinding.inflate(inflater, container, false)
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
                    if (it.data is User) {
                        Logger.log(TAG, "Inside Success of user data")
                        navigateToSearchSuccessFragment(it.data)
                    }
                }

                is Result.Error -> showSnackBar(it.message.toString())
            }
        })
    }

    private fun showSnackBar(message: String) {
        Logger.log(TAG, "Error searching user : $message")
        Snackbar.make(requireView(), "Cannot find user", Snackbar.LENGTH_LONG)
            .setAction(R.string.ok) {
            }.show()
    }

    private fun navigateToSearchSuccessFragment(data: User) {
        val action =
            SearchUserFragmentDirections.actionSearchUserFragmentToUserSearchSuccessFragment(data)
        findNavController().navigate(action)
    }

}
