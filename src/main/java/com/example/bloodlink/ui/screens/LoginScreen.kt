package com.example.bloodlink.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bloodlink.R
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowInputBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.AuthState
import com.example.bloodlink.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { 
                viewModel.signInWithGoogle(it) 
            }
        } catch (e: ApiException) {
            val message = when (e.statusCode) {
                10 -> "Developer Error: Please verify SHA-1 in Firebase Console"
                else -> "Error: ${e.statusCode}"
            }
            viewModel.setError(message)
        }
    }

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("279191894669-n8dnkkfn5rmiabp0rt4hv5rcpbt3ojsm.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        } else if (authState is AuthState.NeedsProfileCompletion) {
            onNavigateToCompleteProfile()
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
        Spacer(modifier = Modifier.height(20.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("💧", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LifeFlow",
                style = MaterialTheme.typography.headlineSmall,
                color = LifeFlowRed,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF101828),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your contribution saves lives.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF667085)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Toggle
        Surface(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigateToSignup() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("SIGN UP", color = Color(0xFF667085), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LOGIN", color = LifeFlowRed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(40.dp).height(2.dp).background(LifeFlowRed))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email Address", color = Color(0xFF667085)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LifeFlowInputBg,
                unfocusedContainerColor = LifeFlowInputBg,
                disabledContainerColor = LifeFlowInputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = Color(0xFF667085)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LifeFlowInputBg,
                unfocusedContainerColor = LifeFlowInputBg,
                disabledContainerColor = LifeFlowInputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Login Account", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
        
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = LifeFlowRed,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "OR CONTINUE WITH",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF98A2B3)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                viewModel.resetState()
                googleSignInLauncher.launch(googleSignInClient.signInIntent) 
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFD0D5DD))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sign in with Google", color = Color(0xFF101828))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
