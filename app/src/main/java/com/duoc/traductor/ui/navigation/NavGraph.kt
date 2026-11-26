package com.duoc.traductor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.traductor.ui.auth.AuthScreen
import com.duoc.traductor.ui.history.HistoryScreen
import com.duoc.traductor.ui.translate.TranslateScreen

object Routes {
    const val AUTH = "auth"
    const val TRANSLATE = "translate"
    const val HISTORY = "history"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.AUTH) {
        composable(Routes.AUTH) {
            AuthScreen(onLoggedIn = {
                nav.navigate(Routes.TRANSLATE) { popUpTo(Routes.AUTH) { inclusive = true } }
            })
        }
        composable(Routes.TRANSLATE) {
            TranslateScreen(
                onHistory = { nav.navigate(Routes.HISTORY) },
                onLogout = {
                    nav.navigate(Routes.AUTH) { popUpTo(Routes.TRANSLATE) { inclusive = true } }
                }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(onBack = { nav.popBackStack() })
        }
    }
}
