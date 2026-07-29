package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestStatusScreen(
    onBack: () -> Unit,
    viewModel: BloodViewModel = viewModel()
) {
    val myRequests by viewModel.getMyRequests().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Status", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LifeFlowBg)
                .padding(padding)
        ) {
            if (myRequests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No active requests", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(myRequests) { request ->
                        RequestStatusCard(
                            requestId = request.id.takeLast(6),
                            bloodGroup = request.bloodGroup,
                            hospital = request.hospitalName,
                            urgency = request.urgency,
                            status = request.status
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestStatusCard(
    requestId: String,
    bloodGroup: String,
    hospital: String,
    urgency: String,
    status: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Request #$requestId",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828),
                    fontSize = 16.sp
                )
                Surface(
                    color = if (status == "Pending") Color(0xFFFFFAEB) else Color(0xFFECFDF3),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (status == "Pending") Color(0xFFB54708) else Color(0xFF027A48),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFE5E6), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(bloodGroup, color = LifeFlowRed, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(hospital, fontWeight = FontWeight.SemiBold, color = Color(0xFF344054))
                    Text("Urgency: $urgency", color = Color(0xFF667085), fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            val progress = if (status == "Pending") 0.4f else 1.0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFF2F4F7), RoundedCornerShape(4.dp)),
                color = if (status == "Pending") LifeFlowRed else Color(0xFF039855),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (status == "Pending") "Searching for nearby donors..." else "Request fulfilled",
                fontSize = 12.sp,
                color = Color(0xFF667085)
            )
        }
    }
}
