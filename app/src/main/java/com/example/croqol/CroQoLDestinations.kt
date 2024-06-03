package com.example.croqol

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.ui.graphics.vector.ImageVector

interface CroQoLDestination {
    val icon: ImageVector
    val route: String
}

object Overview : CroQoLDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Home
    override val route = "overview"
}

object Info : CroQoLDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Info
    override val route = "info"
}

object Login : CroQoLDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.AccountCircle
    override val route = "login"
}

object Review : CroQoLDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.LocationCity
    override val route = "review"
}

val croQoLTabRowScreens = listOf(Overview, Info)
val croQolTabRowScreensLoggedIn = listOf(Overview, Info, Review)