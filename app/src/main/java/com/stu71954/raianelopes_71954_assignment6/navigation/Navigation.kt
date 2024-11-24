package com.stu71954.raianelopes_71954_assignment6.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stu71954.raianelopes_71954_assignment6.home.HomeScreen
import com.stu71954.raianelopes_71954_assignment6.loginUser.LoginScreen
import com.stu71954.raianelopes_71954_assignment6.maps.MapScreen


//Here I put the AppNavigation composable function that is responsible for displaying the Navigation of the application using the NavHost composable function.
// Here it handles the navigation between the Login Screen and the Home Screen.
// The NavHost composable function is used to define the navigation graph and the start destination.
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