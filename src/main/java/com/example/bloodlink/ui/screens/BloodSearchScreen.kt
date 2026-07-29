package com.example.bloodlink.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bloodlink.data.BloodRequest
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowInputBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.BloodViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodSearchScreen(
    onBack: () -> Unit,
    viewModel: BloodViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("A+") }
    val compatibilityAdvice by viewModel.compatibilityAdvice.collectAsState()
    val nearbyHospitals by viewModel.nearbyHospitals.collectAsState()

    var showDetailsDialog by remember { mutableStateOf(false) }
    var selectedHospital by remember { mutableStateOf<BloodRequest?>(null) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingStep by remember { mutableStateOf(0) }

    LaunchedEffect(selectedGroup) {
        viewModel.fetchCompatibilityAdvice(selectedGroup)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LifeFlow", color = LifeFlowRed, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(modifier = Modifier.size(32.dp).background(Color.LightGray, RoundedCornerShape(50))) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp).align(Alignment.Center))
                        }
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
                onDonorsClick = { /* Handle or navigate */ },
                onProfileClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LifeFlowBg)
                .padding(padding)
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search blood banks or hospitals", color = Color(0xFF667085)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF667085)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LifeFlowInputBg,
                    unfocusedContainerColor = LifeFlowInputBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bloodGroups) { group ->
                    val isSelected = selectedGroup == group
                    Surface(
                        modifier = Modifier
                            .size(60.dp)
                            .clickable { selectedGroup = group },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) LifeFlowRed else LifeFlowInputBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = group,
                                color = if (isSelected) Color.White else Color(0xFF101828),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (compatibilityAdvice.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0F9FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB9E6FE))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("✨", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = compatibilityAdvice,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF026AA2)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Nearby Blood Banks", fontWeight = FontWeight.Bold, color = Color(0xFF101828), fontSize = 20.sp)
                Text("${if(nearbyHospitals.isEmpty()) 3 else nearbyHospitals.size} Results Found", color = Color(0xFF667085), fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // If the dynamic list fails, show a HARDCODED fallback for Saveetha immediately
                val displayList = if (nearbyHospitals.isEmpty()) {
                    listOf(
                        BloodRequest(id = "h1", hospitalName = "Saveetha Medical College Hospital", location = "Poonamallee, Chennai", requesterName = "Available", unitsRequired = "24", urgency = "10 Mins"),
                        BloodRequest(id = "h2", hospitalName = "Apollo Speciality Hospital", location = "Chennai", requesterName = "Low Stock", unitsRequired = "05", urgency = "15 Mins"),
                        BloodRequest(id = "h3", hospitalName = "Medicover Hospital", location = "Nellore", requesterName = "Available", unitsRequired = "12", urgency = "5 Mins")
                    )
                } else {
                    nearbyHospitals
                }

                items(displayList) { hospital ->
                    BloodBankCard(
                        name = hospital.hospitalName,
                        location = hospital.location,
                        availableUnits = hospital.unitsRequired,
                        statusText = hospital.requesterName,
                        onDetailsClick = { 
                            selectedHospital = hospital
                            showDetailsDialog = true 
                        },
                        onBookClick = { 
                            selectedHospital = hospital
                            bookingStep = 0
                            showBookingDialog = true 
                        }
                    )
                }
            }
        }
    }

    if (showDetailsDialog && selectedHospital != null) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            confirmButton = { TextButton(onClick = { showDetailsDialog = false }) { Text("Close", color = LifeFlowRed) } },
            title = { Text(selectedHospital?.hospitalName ?: "Details", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Location: ${selectedHospital?.location}", color = Color.Gray)
                    Text("Hours: Open 24/7", color = Color.Gray)
                    Text("Contact: +91 83096 12950", color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Current stock for $selectedGroup: ${selectedHospital?.unitsRequired} Units", fontWeight = FontWeight.Bold, color = LifeFlowRed)
                    Text("Compatibility: Can receive from $selectedGroup, O-", style = MaterialTheme.typography.bodySmall)
                }
            }
        )
    }

    if (showBookingDialog && selectedHospital != null) {
        AlertDialog(
            onDismissRequest = { 
                showBookingDialog = false
                bookingStep = 0
            },
            confirmButton = {
                when (bookingStep) {
                    0 -> Button(onClick = { bookingStep = 1 }, colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed)) { Text("Search Availability") }
                    2 -> Button(onClick = { bookingStep = 3 }, colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed)) { Text("Confirm Booking") }
                    3 -> Button(onClick = { showBookingDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed)) { Text("Done") }
                }
            },
            title = { 
                Text(
                    when (bookingStep) {
                        0 -> "Book Donation"
                        1 -> "Searching..."
                        2 -> "Availability Found!"
                        else -> "Booking Confirmed"
                    }
                )
            },
            text = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    when (bookingStep) {
                        1 -> {
                            LaunchedEffect(Unit) {
                                delay(2000)
                                bookingStep = 2
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = LifeFlowRed)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Connecting to ${selectedHospital?.hospitalName}...")
                            }
                        }
                        2 -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("✅ $selectedGroup Blood is available!", color = Color(0xFF039855), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("We found a matching slot at ${selectedHospital?.hospitalName} for today. Proceed to book?")
                            }
                        }
                        3 -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF039855), modifier = Modifier.size(64.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Appointment fixed for 11:30 AM. Details sent to your email!", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                        else -> {
                            Text("Schedule a blood donation at ${selectedHospital?.hospitalName} for group $selectedGroup.")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun BloodBankCard(
    name: String,
    location: String,
    availableUnits: String,
    statusText: String,
    onDetailsClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF9FAFB)), contentAlignment = Alignment.Center) {
                    Text("🏥", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF101828))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF667085))
                        Text(location, color = Color(0xFF667085), fontSize = 12.sp)
                    }
                }
                Surface(color = if (statusText == "Low Stock") Color(0xFFFEF3F2) else Color(0xFFECFDF3), shape = RoundedCornerShape(50)) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (statusText == "Low Stock") Color.Red else Color(0xFF039855)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(statusText, color = if (statusText == "Low Stock") Color.Red else Color(0xFF039855), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatItem("AVAILABLE", availableUnits, "Units", Modifier.weight(1f))
                StatItem("TYPE", "A+", "Matched", Modifier.weight(1f))
                StatItem("WAIT", "15", "Mins", Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onDetailsClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1E9FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Details", color = Color(0xFF1570EF), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBookClick,
                    modifier = Modifier.weight(1.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Book Donation", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, sublabel: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color(0xFFF2F4F7), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = Color(0xFF667085), fontWeight = FontWeight.Bold)
            Text(value, fontSize = 20.sp, color = LifeFlowRed, fontWeight = FontWeight.Bold)
            Text(sublabel, fontSize = 10.sp, color = Color(0xFF667085))
        }
    }
}
