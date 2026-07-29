package com.example.bloodlink.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.BloodViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DonorLocatorScreen(
    onBack: () -> Unit,
    onNavigateToChat: (String, String) -> Unit,
    viewModel: BloodViewModel = viewModel()
) {
    val context = LocalContext.current
    val nearbyHospitals by viewModel.nearbyHospitals.collectAsState()
    
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    // Default center to Chennai/Nellore region
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(13.0827, 80.2707), 10f)
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        userLocation = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 14f)
                        viewModel.updateLocation(context, location.latitude, location.longitude)
                    }
                }
            } catch (e: SecurityException) { }
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LifeFlow", color = LifeFlowRed, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(modifier = Modifier.size(32.dp).background(Color.LightGray, RoundedCornerShape(50)))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = LifeFlowRed)
                    }
                }
            )
        },
        bottomBar = {
            LifeFlowBottomNavigation(
                onHomeClick = onBack,
                onSearchClick = {},
                onEmergencyClick = { /* Handle or navigate */ },
                onDonorsClick = {},
                onProfileClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LifeFlowBg)
                .padding(padding)
        ) {
            // Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = permissionState.allPermissionsGranted,
                        mapType = MapType.NORMAL
                    ),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true)
                ) {
                    // Always show markers even if userLocation is null
                    val center = userLocation ?: LatLng(13.0827, 80.2707)
                    
                    if (userLocation != null) {
                        Marker(
                            state = rememberMarkerState(position = center),
                            title = "You are here",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                        )
                    }

                    // Display all hospitals from the list on the map
                    nearbyHospitals.forEachIndexed { index, hospital ->
                        val hospPos = LatLng(center.latitude + (index * 0.005) - 0.007, center.longitude + (index * 0.003) - 0.005)
                        Marker(
                            state = rememberMarkerState(position = hospPos),
                            title = hospital.hospitalName,
                            snippet = "Blood Bank • ${hospital.requesterName}",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )
                    }

                    // Sample Donor Pin
                    Marker(
                        state = rememberMarkerState(position = LatLng(center.latitude + 0.004, center.longitude - 0.003)),
                        title = "James Wilson (Donor)",
                        snippet = "O+ Positive",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                }
                
                FloatingActionButton(
                    onClick = {
                        userLocation?.let { cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f) }
                    },
                    containerColor = Color.White,
                    contentColor = LifeFlowRed,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(48.dp)
                ) {
                    Text("📍", fontSize = 18.sp)
                }
            }
            
            // Resources Sheet
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Box(modifier = Modifier.align(Alignment.CenterHorizontally).width(40.dp).height(4.dp).background(Color(0xFFEAECF0), RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Nearby Resources", fontWeight = FontWeight.Bold, color = Color(0xFF101828), fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val displayList = if (nearbyHospitals.isEmpty()) {
                        listOf(
                            com.example.bloodlink.data.BloodRequest(id = "h1", hospitalName = "Saveetha Medical College Hospital", location = "Poonamallee, Chennai", requesterName = "Available"),
                            com.example.bloodlink.data.BloodRequest(id = "h2", hospitalName = "Medicover Hospital", location = "Nellore", requesterName = "Available")
                        )
                    } else {
                        nearbyHospitals
                    }
                    
                    Text("${displayList.size + 1} results found", color = Color(0xFF667085), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        item {
                            DonorLargeCard(
                                name = "James Wilson",
                                info = "O+ POSITIVE",
                                distance = "0.8km away",
                                onCallClick = { 
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890"))
                                    context.startActivity(intent)
                                },
                                onMessageClick = { onNavigateToChat("donor_1", "James Wilson") }
                            )
                        }
                        items(displayList) { hospital ->
                            HospitalMiniCard(hospital.hospitalName)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HospitalMiniCard(name: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF0F9FF),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB9E6FE))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFF026AA2), CircleShape), contentAlignment = Alignment.Center) {
                Text("🏥", color = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
                Text("Hospital • Open Now", color = Color(0xFF026AA2), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DonorLargeCard(name: String, info: String, distance: String, imageUrl: String? = null, onCallClick: () -> Unit, onMessageClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF2F4F7)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (imageUrl != null) {
                    AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray))
                } else {
                    Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray)) {
                        Text("👤", modifier = Modifier.align(Alignment.Center), fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF101828))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📍", fontSize = 12.sp)
                        Text(distance, color = Color(0xFF667085), fontSize = 12.sp)
                    }
                }
                Surface(color = Color(0xFFFFE5E6), shape = RoundedCornerShape(50)) {
                    Text(text = info, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = LifeFlowRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onCallClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F4F7)), shape = RoundedCornerShape(12.dp)) {
                    Text("📞 Call", color = Color(0xFF101828))
                }
                Button(onClick = onMessageClick, modifier = Modifier.weight(1.5f), colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed), shape = RoundedCornerShape(12.dp)) {
                    Text("✉️ Message", color = Color.White)
                }
            }
        }
    }
}
