package com.example.android.creativeim.utils

import androidx.fragment.app.Fragment
import com.example.android.creativeim.CreativeIMApplication

fun Fragment.getViewModelFactory() : ViewModelFactory {
    val repo = (requireContext().applicationContext as CreativeIMApplication).loginRepo
    val viewModelFactory by lazy {
        Logger.log("ViewModelFactoryEx", "Inside to create a new viewModel")
        ViewModelFactory(repo, this)
    }
    return viewModelFactory
}