package com.stu71954.raianelopes_71954_assignment6.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var latitude by remember { mutableStateOf(TextFieldValue("")) }
    var longitude by remember { mutableStateOf(TextFieldValue("")) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    // Remember the camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 5f)
    }
    // Request location updates when the location permissions are granted
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
                    userLocation = locationResult.lastLocation
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }, null)
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
    // Update the camera position when the marker position changes
    LaunchedEffect(markerPosition) {
        markerPosition?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLng(it))
        }
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            Button(
                onClick = {
                    val lat = latitude.text.toDoubleOrNull()
                    val lng = longitude.text.toDoubleOrNull()
                    if (lat != null && lng != null) {
                        val newMarkerPosition = LatLng(lat, lng)
                        markerPosition = newMarkerPosition
                        Log.d("MapScreen", "Marker set to: $lat, $lng")
                    } else {
                        Log.d("MapScreen", "Invalid latitude or longitude")
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
            ) {
                Text("Set Marker")
            }
            // Display the map if the location permissions are granted
            if (locationPermissionsState.allPermissionsGranted) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    markerPosition?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Custom Location",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE
                            )
                        )
                    }
                }
            } else {
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
}