package com.example.croqol.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.croqol.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userStore: DataStore<User>
) {

    private val TAG: String = "UserRepository"
    val users = mapOf("slezo" to "slezo", "matej" to "12345678")

    val userFlow: Flow<User> = userStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(User.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateUser(username: String) {
        Log.d(TAG, "Updating user $username")
        userStore.updateData { preferences ->
            preferences.toBuilder().setUsername(username).setIsLoggedIn(true).build()
        }
    }

    suspend fun logOutUser() {
        userStore.updateData { preferences ->
            preferences.toBuilder().setUsername("").setIsLoggedIn(false).build()
        }
    }

}