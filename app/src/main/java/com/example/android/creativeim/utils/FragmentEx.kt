package com.example.android.creativeim.utils

import androidx.fragment.app.Fragment
import com.example.android.creativeim.CreativeIMApplication

fun Fragment.getViewModelFactory() : ViewModelFactory {
    val repo = (requireContext().applicationContext as CreativeIMApplication).loginRepo
    val viewModelFactory by lazy {
        ViewModelFactory(repo, this)
    }
    return viewModelFactory
}