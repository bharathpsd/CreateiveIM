package com.example.android.creativeim.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.creativeim.databinding.FragmentSplashBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {
    private val TAG = "SplashFragment"
    private lateinit var viewBinding : FragmentSplashBinding

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewmodel = viewModel
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setClickListeners()
    }

    private fun setClickListeners() {
        Logger.log(TAG, "Inside click Listeners")
        viewModel._hasUser.let {
            if(it.value!!) {
                Logger.log(TAG, "${it.value}")
                navigateToOtherFragment(3)
            }
        }
        sign_in.setOnClickListener {
            Logger.log(TAG, "Inside sign_in Listeners")
            navigateToOtherFragment(2)
        }
        registration.setOnClickListener {
            Logger.log(TAG, "Inside registration Listeners")
            navigateToOtherFragment(1)
        }
    }

    private fun navigateToOtherFragment(screen: Int) {
        Logger.log(TAG, "Screen on click : $screen")
        when(screen) {
            1 -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
            2 -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            3 -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
            else -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
        }
    }

}
