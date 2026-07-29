package com.example.bloodlink.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bloodlink.R
import com.example.bloodlink.data.User
import com.example.bloodlink.ui.theme.LifeFlowBg
import com.example.bloodlink.ui.theme.LifeFlowInputBg
import com.example.bloodlink.ui.theme.LifeFlowRed
import com.example.bloodlink.ui.viewmodels.AuthState
import com.example.bloodlink.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("A+") }
    
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onSignupSuccess()
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
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
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

        Spacer(modifier = Modifier.height(30.dp))
        
        // Profile Image Picker
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = LifeFlowInputBg
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👤", fontSize = 40.sp)
                    }
                }
            }
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = LifeFlowRed,
                shadowElevation = 4.dp
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Pick Image",
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Join the Community",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF101828),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Toggle
        Surface(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SIGN UP", color = LifeFlowRed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(50.dp).height(2.dp).background(LifeFlowRed))
                    }
                }
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigateToLogin() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("LOGIN", color = Color(0xFF667085), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        LifeFlowTextField(value = name, onValueChange = { name = it }, placeholder = "Full Name")
        Spacer(modifier = Modifier.height(16.dp))
        LifeFlowTextField(value = email, onValueChange = { email = it }, placeholder = "Email Address")
        Spacer(modifier = Modifier.height(16.dp))
        LifeFlowTextField(value = password, onValueChange = { password = it }, placeholder = "Password", isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))
        LifeFlowTextField(value = location, onValueChange = { location = it }, placeholder = "Current City", leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF667085), modifier = Modifier.size(20.dp)) })

        Spacer(modifier = Modifier.height(24.dp))
        
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

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { 
                val user = User(
                    name = name, 
                    email = email, 
                    bloodGroup = bloodGroup, 
                    location = location,
                    profileImageUrl = selectedImageUri?.toString() ?: ""
                )
                viewModel.signup(user, password) 
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeFlowRed),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Create Account", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

        Spacer(modifier = Modifier.height(24.dp))
        
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
                Text("Sign up with Google", color = Color(0xFF101828))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LifeFlowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF667085)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = leadingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = LifeFlowInputBg,
            unfocusedContainerColor = LifeFlowInputBg,
            disabledContainerColor = LifeFlowInputBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default
    )
}
