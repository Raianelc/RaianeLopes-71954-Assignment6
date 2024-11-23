package com.stu71954.raianelopes_71954_assignment6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.stu71954.raianelopes_71954_assignment6.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}