package com.example.croqol.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.croqol.R
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
) : ViewModel() {

    var userIsAuthenticated by mutableStateOf(false)

    private val TAG: String = "MainViewModel"
    private lateinit var account: Auth0

    fun setAccount(context: Context) {
        account = Auth0(
            context.getString(R.string.com_auth0_client_id),
            context.getString(R.string.com_auth0_domain)
        )
    }

    fun login(context: Context) {
        WebAuthProvider
            .login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e(TAG, "Error in login: $error")
                }

                override fun onSuccess(result: Credentials) {
                    val idToken = result.idToken

                    userIsAuthenticated = true
                }
            })
    }

    fun logout(context: Context) {
        WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object: Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e(TAG, "Error in logout: $error")
                }

                override fun onSuccess(result: Void?) {
                    userIsAuthenticated = false
                }
            })
    }

    // Old Login
    /*
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
    */
}