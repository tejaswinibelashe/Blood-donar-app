package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("LifeFlow", color = LifeFlowRed, fontWeight = FontWeight.Bold) 
                },
                navigationIcon = {
                    Box(modifier = Modifier.padding(start = 16.dp).size(32.dp).background(Color.LightGray, RoundedCornerShape(50)))
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
                onHomeClick = {},
                onSearchClick = {},
                onEmergencyClick = {},

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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Inventory Analytics",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF101828),
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("TOTAL RESERVES", fontSize = 12.sp, color = Color(0xFF667085), fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("1,248", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = LifeFlowRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Units", fontSize = 18.sp, color = LifeFlowRed)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("📈 12%", color = Color(0xFF039855), fontWeight = FontWeight.Bold)
                    }
                    Text("VS LAST WEEK", fontSize = 10.sp, color = Color(0xFF98A2B3))
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Analytics Bar Chart (Simulated)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        AnalyticsBar("A+", 0.8f)
                        AnalyticsBar("A-", 0.4f)
                        AnalyticsBar("B+", 0.6f)
                        AnalyticsBar("O-", 0.2f)
                        AnalyticsBar("AB+", 0.9f)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Donor Registry",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF101828),
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("DONOR", modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color(0xFF667085), fontWeight = FontWeight.Bold)
                        Text("BLOOD TYPE", fontSize = 12.sp, color = Color(0xFF667085), fontWeight = FontWeight.Bold)
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF2F4F7))
                    
                    DonorRow("Marcus Sterling", "marcus.s@provider.com", "O+")
                    DonorRow("Elena Rodriguez", "elena.rod@mail.com", "A-")
                    DonorRow("Julian Chen", "j.chen@health.org", "B+")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("<", color = Color(0xFF667085))
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(LifeFlowRed), contentAlignment = Alignment.Center) {
                            Text("1", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("2", color = Color(0xFF667085))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(">", color = Color(0xFF667085))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AnalyticsBar(label: String, progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFEFF8FF)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(progress)
                    .background(LifeFlowRed)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 10.sp, color = Color(0xFF667085), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DonorRow(name: String, email: String, type: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
            Text(email, color = Color(0xFF667085), fontSize = 12.sp)
        }
        Surface(color = Color(0xFFFFE5E6), shape = RoundedCornerShape(8.dp)) {
            Text(
                text = type,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = LifeFlowRed,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
    HorizontalDivider(color = Color(0xFFF2F4F7))
}
