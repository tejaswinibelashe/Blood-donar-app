package com.example.bloodlink.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.R
import com.example.bloodlink.ui.theme.LifeFlowRed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(800, easing = OvershootInterpolator(2f)::getInterpolation)
        )
        delay(2000L)
        onNavigateToLogin()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo Container
            Surface(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale.value),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_blood_drop),
                        contentDescription = "Logo",
                        tint = LifeFlowRed,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "LifeFlow",
                style = MaterialTheme.typography.headlineMedium,
                color = LifeFlowRed,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            
            Text(
                text = "BLOODLINK ECOSYSTEM",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF667085),
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE5E6)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = LifeFlowRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Every Drop Saves Lives",
                        color = LifeFlowRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        // Version info at bottom
        Text(
            text = "v2.4.0 • CLINICAL GRADE",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            color = Color(0xFF98A2B3),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

class OvershootInterpolator(private val tension: Float) {
    fun getInterpolation(t: Float): Float {
        val tValue = t - 1.0f
        return tValue * tValue * ((tension + 1) * tValue + tension) + 1.0f
    }
}
