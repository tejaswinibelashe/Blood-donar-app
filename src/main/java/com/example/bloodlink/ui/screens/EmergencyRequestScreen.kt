package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.BloodViewModel
import com.example.bloodlink.ui.viewmodels.RequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRequestScreen(
    onBack: () -> Unit,
    onNavigateToSuccess: (String, String, String, String, String, String, String) -> Unit,
    viewModel: BloodViewModel = viewModel()
) {
    var patientName by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var hospital by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var units by remember { mutableStateOf("1") }
    var urgency by remember { mutableStateOf("HIGH") }
    
    val requestState by viewModel.requestState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("LifeFlow", color = LifeFlowRed, fontWeight = FontWeight.Bold) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = LifeFlowRed)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = LifeFlowRed)
                    }
                    Box(
                        modifier = Modifier.padding(end = 16.dp).size(32.dp).background(Color.LightGray, RoundedCornerShape(50))
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LifeFlowBg)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f).height(4.dp).background(LifeFlowRed, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f).height(4.dp).background(LifeFlowRed, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f).height(4.dp).background(Color(0xFFEAECF0), RoundedCornerShape(2.dp)))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Emergency Request",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF101828),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fill in the details below to initiate an urgent blood supply request.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF667085)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SectionTitle("0. Patient Details")
            Spacer(modifier = Modifier.height(16.dp))
            LifeFlowInput(value = patientName, onValueChange = { patientName = it }, placeholder = "Patient Full Name")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SectionTitle("1. Select Blood Group")
            Spacer(modifier = Modifier.height(16.dp))
            
            val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(140.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(bloodGroups) { group ->
                    val isSelected = bloodGroup == group
                    Surface(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { bloodGroup = group }
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) LifeFlowRed else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = group,
                                color = if (isSelected) LifeFlowRed else Color(0xFFBC1C24),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SectionTitle("2. Hospital & Units")
            Spacer(modifier = Modifier.height(16.dp))
            
            LifeFlowInput(value = hospital, onValueChange = { hospital = it }, placeholder = "Enter hospital name", trailingIcon = { Icon(Icons.Default.Add, contentDescription = null) })
            Spacer(modifier = Modifier.height(16.dp))
            LifeFlowInput(value = location, onValueChange = { location = it }, placeholder = "Hospital Location / Address")
            Spacer(modifier = Modifier.height(16.dp))
            LifeFlowInput(value = units, onValueChange = { units = it }, placeholder = "Units Required")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SectionTitle("3. Urgency Level")
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                UrgencyCard("LOW", "Within 24h", urgency == "LOW", Modifier.weight(1f)) { urgency = "LOW" }
                UrgencyCard("MEDIUM", "Within 6h", urgency == "MEDIUM", Modifier.weight(1f)) { urgency = "MEDIUM" }
                UrgencyCard("HIGH", "Immediate", urgency == "HIGH", Modifier.weight(1f)) { urgency = "HIGH" }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { 
                    viewModel.submitEmergencyRequest(patientName, bloodGroup, hospital, location, units, urgency) { requestId ->
                        onNavigateToSuccess(requestId, patientName, bloodGroup, hospital, location, units, urgency)
                    } 
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed),
                enabled = requestState !is RequestState.Loading && bloodGroup.isNotEmpty() && hospital.isNotEmpty() && patientName.isNotEmpty()
            ) {
                if (requestState is RequestState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Confirm & Broadcast", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF101828),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
fun LifeFlowInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF667085)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF2F4F7),
            unfocusedContainerColor = Color(0xFFF2F4F7),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun UrgencyCard(
    level: String,
    time: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) LifeFlowRed else Color(0xFFD0D5DD),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) LifeFlowRed else Color.White
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(level, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color(0xFF101828), fontSize = 12.sp)
            Text(time, color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color(0xFF667085), fontSize = 12.sp)
        }
    }
}
