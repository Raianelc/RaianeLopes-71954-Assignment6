package com.stu71954.raianelopes_71954_assignment6.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.stu71954.raianelopes_71954_assignment6.maps.MapScreen


//Her I put the HomeScreen composable function that is responsible for displaying the Home Screen of the application using the Scaffold composable function.
//I also prefer to call the MapScreen composable function inside the Scaffold composable function to display the map on the Home Screen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home Screen") }
            )
        }
    ) { paddingValues ->
        MapScreen(modifier = Modifier.padding(paddingValues))
    }
}