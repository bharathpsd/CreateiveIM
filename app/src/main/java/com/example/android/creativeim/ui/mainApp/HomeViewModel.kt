package com.example.android.creativeim.ui.mainApp

import androidx.lifecycle.ViewModel
import com.example.android.creativeim.repo.LoginRepoInterface

class HomeViewModel(
    private val repo : LoginRepoInterface
) : ViewModel() {
}