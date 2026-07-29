package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RequestSuccessScreen(
    requestId: String,
    patientName: String,
    bloodGroup: String,
    hospitalName: String,
    location: String,
    units: String,
    urgency: String,
    onTrackRequest: () -> Unit,
    onContactSupport: () -> Unit,
    onReturnHome: () -> Unit
) {
    val currentTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeFlowBg)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Success Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFECFDF3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF039855),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Emergency Request Submitted Successfully",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Request Details Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                DetailRow("Request ID", "#$requestId", isBold = true)
                DetailRow("Patient Name", patientName)
                DetailRow("Blood Group", bloodGroup, color = LifeFlowRed)
                DetailRow("Hospital Name", hospitalName)
                DetailRow("Location", location)
                DetailRow("Units Required", units)
                DetailRow("Emergency Level", urgency, color = if(urgency == "HIGH") LifeFlowRed else Color(0xFFB54708))
                DetailRow("Date and Time", currentTime)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Searching Indicator
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF9FAFB),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAECF0))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = LifeFlowRed
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Searching Nearby Donors",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF344054)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Estimated Response Time: ~15 mins",
                    fontSize = 14.sp,
                    color = Color(0xFF667085)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Buttons
        Button(
            onClick = onTrackRequest,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed)
        ) {
            Text("Track Request", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onContactSupport,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0D5DD))
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contact Support", color = Color(0xFF344054))
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onReturnHome) {
            Text("Return to Dashboard", color = Color(0xFF667085), fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun DetailRow(label: String, value: String, isBold: Boolean = false, color: Color = Color(0xFF101828)) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFF667085), fontSize = 14.sp)
        Text(
            text = value,
            color = color,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
    }
}
