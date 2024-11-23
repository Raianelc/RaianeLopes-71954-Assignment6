package com.stu71954.raianelopes_71954_assignment6.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stu71954.raianelopes_71954_assignment6.home.HomeScreen
import com.stu71954.raianelopes_71954_assignment6.loginUser.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeScreen() }
        }
    }
}