package com.example.bloodlink.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object CompleteProfile : Screen("complete_profile")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object BloodSearch : Screen("blood_search")
    object DonorLocator : Screen("donor_locator")
    object EmergencyRequest : Screen("emergency_request")
    object RequestStatus : Screen("request_status")
    object AdminDashboard : Screen("admin_dashboard")
    object RequestSuccess : Screen("request_success/{requestId}/{patientName}/{bloodGroup}/{hospital}/{location}/{units}/{urgency}") {
        fun createRoute(id: String, name: String, group: String, hosp: String, loc: String, units: String, urg: String) = 
            "request_success/$id/$name/$group/$hosp/$loc/$units/$urg"
    }
    object Chat : Screen("chat/{userId}/{userName}") {
        fun createRoute(userId: String, userName: String) = "chat/$userId/$userName"
    }
}
