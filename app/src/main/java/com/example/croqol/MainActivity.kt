package com.example.croqol

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.croqol.ui.CroQoLNavHost
import com.example.croqol.ui.MainViewModel
import com.example.croqol.ui.navigateSingleTopTo
import com.example.croqol.ui.theme.CroQoLTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CroQoLApp()
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CroQoLApp(

) {
    CroQoLTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = croQoLTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
        val mainViewModel: MainViewModel = hiltViewModel()
        val user by mainViewModel.userFlow.collectAsStateWithLifecycle(initialValue = User.getDefaultInstance())
        val screens = if (user.isLoggedIn) croQolTabRowScreensLoggedIn else croQoLTabRowScreens
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                screens.forEach {
                    item(
                        icon = {
                            Icon(imageVector = it.icon, contentDescription = it.route)
                        },
                        label = { Text(it.route) },
                        selected = it == currentScreen,
                        onClick = {
                            navController.navigateSingleTopTo(it.route)
                        }
                    )
                }
                if (user.isLoggedIn) {
                    item(
                        icon = {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "")
                        },
                        label = { Text("Logout") },
                        selected = false,
                        onClick = {
                            mainViewModel.logOutUser()
                            navController.navigateSingleTopTo(Overview.route)
                        }
                    )
                }
            }
        ) {
            CroQoLNavHost(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}
