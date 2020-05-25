package com.example.android.creativeim.utils

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.android.creativeim.repo.LoginRepoInterface
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.ui.mainApp.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val loginRepo: LoginRepoInterface,
    owner: SavedStateRegistryOwner,
    defaultArgs : Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) -> MainViewModel(loginRepo)
            isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(loginRepo)
            else -> MainViewModel(loginRepo)
        }
    } as T

}