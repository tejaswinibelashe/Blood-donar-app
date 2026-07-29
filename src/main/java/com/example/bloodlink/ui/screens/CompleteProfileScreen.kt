package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.bloodlink.ui.theme.LifeFlowInputBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.AuthState
import com.example.bloodlink.ui.viewmodels.AuthViewModel

@Composable
fun CompleteProfileScreen(
    onComplete: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var phone by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("A+") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onComplete()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeFlowBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = "Complete Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF101828),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Almost there! We need a few more details.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF667085)
        )

        Spacer(modifier = Modifier.height(40.dp))

        LifeFlowTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = "Phone Number"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "SELECT BLOOD GROUP",
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF344054),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(140.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(bloodGroups) { group ->
                val isSelected = bloodGroup == group
                Surface(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { bloodGroup = group },
                    shape = RoundedCornerShape(8.dp),
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

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.completeProfile(phone, bloodGroup) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed),
            enabled = authState !is AuthState.Loading && phone.isNotEmpty()
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Complete & Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}
