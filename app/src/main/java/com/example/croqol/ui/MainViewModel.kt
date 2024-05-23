package com.example.croqol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.croqol.User
import com.example.croqol.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val TAG: String = "MainViewModel"
    val userFlow = userRepository.userFlow

    private fun updateUser(username: String) {
        viewModelScope.launch {
            userRepository.updateUser(username)
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            userRepository.logOutUser()
        }
    }

    fun logIn(): Boolean {
        Log.d(TAG, "Logging in user $userName with password $passWord")
        if (!userRepository.users.containsKey(userName)) {
            loginError = "Unknown Username and Password Combination"
            return false
        }
        if (userRepository.users[userName] != passWord) {
            loginError = "Unknown Username and Password Combination"
            return false
        }
        updateUser(userName)
        userName = ""
        passWord = ""
        loginError = ""
        return true
    }

    var userName by mutableStateOf("")
        private set
    var passWord by mutableStateOf("")
        private set
    var loginError by mutableStateOf("")
        private set

    fun updateUserName(input: String) {
        userName = input
    }

    fun updatePassWord(input: String) {
        passWord = input
    }

}