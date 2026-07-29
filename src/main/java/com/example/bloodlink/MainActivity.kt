package com.example.bloodlink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.screens.*
import com.example.bloodlink.ui.theme.BloodLinkTheme
import com.example.bloodlink.ui.viewmodels.BloodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloodLinkTheme {
                BloodLinkApp()
            }
        }
    }
}

@Composable
fun BloodLinkApp() {
    val navController = rememberNavController()
    val bloodViewModel: BloodViewModel = viewModel() // SHARED VIEWMODEL

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onNavigateToCompleteProfile = {
                    navController.navigate(Screen.CompleteProfile.route)
                }
            )
        }
        
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToCompleteProfile = {
                    navController.navigate(Screen.CompleteProfile.route)
                }
            )
        }

        composable(Screen.CompleteProfile.route) {
            CompleteProfileScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.BloodSearch.route) },
                onNavigateToEmergency = { navController.navigate(Screen.EmergencyRequest.route) },
                onNavigateToLocator = { navController.navigate(Screen.DonorLocator.route) },
                onNavigateToChat = { userId, userName -> 
                    navController.navigate(Screen.Chat.createRoute(userId, userName))
                },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                viewModel = bloodViewModel
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                bloodViewModel = bloodViewModel
            )
        }
        
        composable(Screen.BloodSearch.route) {
            BloodSearchScreen(
                onBack = { navController.popBackStack() },
                viewModel = bloodViewModel
            )
        }
        
        composable(Screen.EmergencyRequest.route) {
             EmergencyRequestScreen(
                onBack = { navController.popBackStack() },
                onNavigateToSuccess = { id, name, group, hosp, loc, units, urg ->
                    navController.navigate(Screen.RequestSuccess.createRoute(id, name, group, hosp, loc, units, urg)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                viewModel = bloodViewModel
            )
        }
        
        composable(Screen.DonorLocator.route) {
            DonorLocatorScreen(
                onBack = { navController.popBackStack() },
                onNavigateToChat = { userId, userName -> 
                    navController.navigate(Screen.Chat.createRoute(userId, userName))
                },
                viewModel = bloodViewModel
            )
        }
        
        composable(Screen.RequestStatus.route) {
            RequestStatusScreen(
                onBack = { navController.popBackStack() },
                viewModel = bloodViewModel
            )
        }
        
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen()
        }

        composable(
            route = Screen.RequestSuccess.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType },
                navArgument("patientName") { type = NavType.StringType },
                navArgument("bloodGroup") { type = NavType.StringType },
                navArgument("hospital") { type = NavType.StringType },
                navArgument("location") { type = NavType.StringType },
                navArgument("units") { type = NavType.StringType },
                navArgument("urgency") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            RequestSuccessScreen(
                requestId = backStackEntry.arguments?.getString("requestId") ?: "",
                patientName = backStackEntry.arguments?.getString("patientName") ?: "",
                bloodGroup = backStackEntry.arguments?.getString("bloodGroup") ?: "",
                hospitalName = backStackEntry.arguments?.getString("hospital") ?: "",
                location = backStackEntry.arguments?.getString("location") ?: "",
                units = backStackEntry.arguments?.getString("units") ?: "",
                urgency = backStackEntry.arguments?.getString("urgency") ?: "",
                onTrackRequest = { navController.navigate(Screen.RequestStatus.route) },
                onContactSupport = { /* Action to support */ },
                onReturnHome = { navController.navigate(Screen.Home.route) { popUpTo(0) } }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ChatScreen(
                otherUserId = userId,
                otherUserName = userName,
                onBack = { navController.popBackStack() },
                viewModel = bloodViewModel
            )
        }
    }
}
