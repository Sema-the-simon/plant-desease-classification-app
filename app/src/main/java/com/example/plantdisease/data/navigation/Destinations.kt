package com.example.plantdisease.data.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

object Home : Destination {
    override val route: String = "home"
}
object Camera : Destination {
    override val route: String = "camera"
}
object SelectedImage : Destination {
    override val route: String = "image"
    const val uri = "uri"
    const val routeWithArgs = "image/{uri}"

    val arguments = listOf(
        navArgument(uri) {
            type = NavType.StringType
        }
    )
    fun navToOrderWithArgs(
        uri: String = ""
    ): String {
        return "$route/$uri"
    }
}

