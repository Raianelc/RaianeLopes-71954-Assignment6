package com.stu71954.raianelopes_71954_assignment6.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Here I put the MapScreen composable function that is responsible for displaying the Map Screen of the application.
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    // State to hold the user's location
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current

    // State to manage location permissions
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            // Permission to access the coarse location
            Manifest.permission.ACCESS_COARSE_LOCATION,
            // Permission to access the fine location
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    // Effect to request location updates when permissions are granted
    LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1000)
                .build()
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // Update the user's location
                    userLocation = locationResult.lastLocation
                    // Remove location updates after getting the location
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }, null)
        } else {
            // Request permissions if not granted
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    // Box to contain the map or permission request UI
    Box(modifier = modifier) {
        if (locationPermissionsState.allPermissionsGranted) {
            userLocation?.let {
                // Display the map with the user's location
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            LatLng(it.latitude, it.longitude), 15f
                        )
                    }
                ) {
                    // Add a marker at the user's location
                    Marker(
                        state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                        title = "My Location"
                    )
                }
            } ?: run {
                // Display a message while fetching the location
                Text("Fetching location...", modifier = Modifier.fillMaxSize())
            }
        } else {
            // Display a message and button to request permissions
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Location permissions are required to display the map.")
                Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                    Text("Grant Permissions")
                }
            }
        }
    }
}