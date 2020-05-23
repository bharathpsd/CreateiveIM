package com.example.android.creativeim.ui

import androidx.lifecycle.ViewModel
import com.example.android.creativeim.repo.LoginRepoInterface

class MainViewModel (
    val repo : LoginRepoInterface
) : ViewModel() {
}