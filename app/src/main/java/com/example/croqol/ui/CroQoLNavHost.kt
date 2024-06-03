package com.example.croqol.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.croqol.Info
import com.example.croqol.Overview
import com.example.croqol.Review
import com.example.croqol.ui.info.InfoScreen
import com.example.croqol.ui.overview.OverviewScreen
import com.example.croqol.ui.overview.OverviewViewModel
import com.example.croqol.ui.review.ReviewScreen
import com.example.croqol.ui.review.ReviewViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CroQoLNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val overviewViewModel: OverviewViewModel = hiltViewModel()
    val reviewViewModel: ReviewViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        composable(route = Overview.route) {
            OverviewScreen(
                viewModel = overviewViewModel
            )
        }
        composable(route = Info.route) {
            InfoScreen()
        }
        composable(route = Review.route) {
            ReviewScreen(
                viewModel = reviewViewModel
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }