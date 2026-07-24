package com.example.bloodlink.repository

import com.example.bloodlink.data.BloodRequest
import com.example.bloodlink.data.Message
import com.example.bloodlink.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await

class BloodRepository {
    private val db get() = try { FirebaseDatabase.getInstance().reference } catch (e: Exception) { null }
    private val usersRef get() = db?.child("users")
    private val requestsRef get() = db?.child("requests")
    private val messagesRef get() = db?.child("messages")

    suspend fun getNearbyHospitals(city: String): List<BloodRequest> {
        val hospitals = mutableListOf<BloodRequest>()
        
        // Real-world names for Chennai/Nellore
        if (city.contains("Chennai", ignoreCase = true) || city.contains("Poonamallee", ignoreCase = true)) {
            hospitals.add(BloodRequest(id = "h1", hospitalName = "Saveetha Medical College Hospital", location = "Poonamallee, Chennai", requesterName = "Available", unitsRequired = "24", urgency = "10 Mins"))
            hospitals.add(BloodRequest(id = "h2", hospitalName = "Apollo Speciality Hospital", location = "OMR, Chennai", requesterName = "Low Stock", unitsRequired = "05", urgency = "15 Mins"))
            hospitals.add(BloodRequest(id = "h3", hospitalName = "MIOT International", location = "Manapakkam, Chennai", requesterName = "Available", unitsRequired = "18", urgency = "20 Mins"))
        } else if (city.contains("Nellore", ignoreCase = true) || city.contains("Simhapuri", ignoreCase = true)) {
            hospitals.add(BloodRequest(id = "n1", hospitalName = "Medicover Hospitals", location = "Nellore Central", requesterName = "Available", unitsRequired = "12", urgency = "5 Mins"))
            hospitals.add(BloodRequest(id = "n2", hospitalName = "Apollo Nellore", location = "Main Road", requesterName = "Low Stock", unitsRequired = "02", urgency = "Urgent"))
            hospitals.add(BloodRequest(id = "n3", hospitalName = "Simhapuri Hospital", location = "Nellore", requesterName = "Available", unitsRequired = "08", urgency = "12 Mins"))
        } else {
            // Default list if city is generic
            hospitals.add(BloodRequest(id = "h1", hospitalName = "Saveetha Medical College Hospital", location = "Chennai", requesterName = "Available", unitsRequired = "24", urgency = "10 Mins"))
            hospitals.add(BloodRequest(id = "d1", hospitalName = "City General Hospital", location = "Main Road", requesterName = "Available", unitsRequired = "14", urgency = "15 Mins"))
        }
        return hospitals
    }

    suspend fun registerUser(user: User) {
        usersRef?.child(user.uid)?.setValue(user)?.await()
    }

    suspend fun createRequest(request: BloodRequest) {
        val ref = requestsRef ?: return
        val newRequestRef = ref.push()
        val id = newRequestRef.key ?: ""
        val newRequest = request.copy(id = id)
        newRequestRef.setValue(newRequest).await()
    }

    suspend fun getNearbyRequests() : List<BloodRequest> {
        return try {
            val ref = requestsRef ?: return emptyList()
            val snapshot = ref.get().await()
            snapshot.children.mapNotNull { it.getValue(BloodRequest::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserDetails(uid: String): User? {
        return try {
            val ref = usersRef ?: return null
            ref.child(uid).get().await().getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateRequestStatus(requestId: String, status: String) {
        requestsRef?.child(requestId)?.child("status")?.setValue(status)?.await()
    }

    fun getMyRequests(userId: String): Flow<List<BloodRequest>> {
        val ref = requestsRef ?: return emptyFlow()
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val requests = snapshot.children.mapNotNull { it.getValue(BloodRequest::class.java) }
                        .filter { it.requesterId == userId }
                        .sortedByDescending { it.timestamp }
                    trySend(requests)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }

    suspend fun sendMessage(message: Message) {
        val ref = messagesRef ?: return
        val msgId = ref.push().key ?: ""
        ref.child(msgId).setValue(message.copy(id = msgId)).await()
    }

    fun getMessages(currentUserId: String, otherUserId: String): Flow<List<Message>> {
        val ref = messagesRef ?: return emptyFlow()
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                        .filter { 
                            (it.senderId == currentUserId && it.receiverId == otherUserId) ||
                            (it.senderId == otherUserId && it.receiverId == currentUserId)
                        }
                        .sortedBy { it.timestamp }
                    trySend(messages)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }
}
