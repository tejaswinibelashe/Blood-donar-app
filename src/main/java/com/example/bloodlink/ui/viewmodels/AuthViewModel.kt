package com.example.bloodlink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.data.User
import com.example.bloodlink.repository.BloodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object NeedsProfileCompletion : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth get() = try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
    private val repository = BloodRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                val firebaseAuth = auth ?: throw Exception("Authentication service unavailable")
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = repository.getUserDetails(result.user?.uid ?: "")
                if (user?.phone?.isEmpty() == true) {
                    _authState.value = AuthState.NeedsProfileCompletion
                } else {
                    _authState.value = AuthState.Success
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login Failed")
            }
        }
    }

    fun signup(user: User, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                val firebaseAuth = auth ?: throw Exception("Authentication service unavailable")
                val result = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
                val uid = result.user?.uid ?: throw Exception("Signup Failed")
                val newUser = user.copy(uid = uid)
                repository.registerUser(newUser)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Signup Failed")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                val firebaseAuth = auth ?: throw Exception("Authentication service unavailable")
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    val existingUser = repository.getUserDetails(firebaseUser.uid)
                    if (existingUser == null) {
                        val newUser = User(
                            uid = firebaseUser.uid,
                            name = firebaseUser.displayName ?: "",
                            email = firebaseUser.email ?: "",
                            profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                            phone = firebaseUser.phoneNumber ?: ""
                        )
                        repository.registerUser(newUser)
                        _authState.value = AuthState.NeedsProfileCompletion
                    } else if (existingUser.phone.isEmpty() || existingUser.bloodGroup.isEmpty()) {
                        _authState.value = AuthState.NeedsProfileCompletion
                    } else {
                        _authState.value = AuthState.Success
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Google Sign-In Failed")
            }
        }
    }

    fun completeProfile(phone: String, bloodGroup: String) {
        val uid = auth?.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                val existingUser = repository.getUserDetails(uid)
                if (existingUser != null) {
                    val updatedUser = existingUser.copy(phone = phone, bloodGroup = bloodGroup)
                    repository.registerUser(updatedUser)
                    _authState.value = AuthState.Success
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to update profile")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
}
