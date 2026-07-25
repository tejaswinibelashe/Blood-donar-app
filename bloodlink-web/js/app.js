import { auth, db, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, ref, push, onValue, set } from './firebase.js';

// DOM Elements
const header = document.getElementById('main-header');
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const searchSection = document.getElementById('search-section');
const requestSection = document.getElementById('request-section');

const loginForm = document.getElementById('login-form');
const authError = document.getElementById('auth-error');
const logoutBtn = document.getElementById('logout-btn');
const authSubmitBtn = document.getElementById('auth-submit-btn');
const authToggleBtn = document.getElementById('auth-toggle-btn');
const authToggleText = document.getElementById('auth-toggle-text');

let isLoginMode = true;

const userDisplayName = document.getElementById('user-display-name');
const recentRequestsCount = document.getElementById('recent-requests-count');
const availableDonorsCount = document.getElementById('available-donors-count');
const urgentRequestsList = document.getElementById('urgent-requests-list');

const searchBloodGroup = document.getElementById('search-blood-group');
const searchBtn = document.getElementById('search-btn');
const searchResults = document.getElementById('search-results');

const bloodRequestForm = document.getElementById('blood-request-form');
const requestStatus = document.getElementById('request-status');

const nearbyDonorsSection = document.getElementById('nearby-donors-section');
const nearbyPatientsSection = document.getElementById('nearby-patients-section');
const hospitalsSection = document.getElementById('hospitals-section');
const nearbyDonorsList = document.getElementById('nearby-donors-list');
const nearbyPatientsList = document.getElementById('nearby-patients-list');
const hospitalsList = document.getElementById('hospitals-list');

const navBtns = document.querySelectorAll('.nav-btn:not(.danger)');

// Navigation Logic
function switchSection(targetId) {
    document.querySelectorAll('main > section.glass-panel').forEach(sec => sec.classList.add('hidden'));
    const targetSec = document.getElementById(targetId);
    if (targetSec) targetSec.classList.remove('hidden');

    navBtns.forEach(btn => {
        if(btn.dataset.target === targetId) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}

navBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
        switchSection(e.currentTarget.dataset.target);
    });
});

// Listen for Auth State Changes
onAuthStateChanged(auth, (user) => {
    if (user) {
        authSection.classList.add('hidden');
        header.classList.remove('hidden');
        document.getElementById('user-display-name').textContent = user.email.split('@')[0];
        
        // Populate profile
        const profileEmail = document.getElementById('profile-email');
        const profileUid = document.getElementById('profile-uid');
        if (profileEmail) profileEmail.textContent = user.email;
        if (profileUid) profileUid.textContent = user.uid;

        switchSection('dashboard-section');
        loadDashboardData();
        loadNearbyData();
    } else {
        // User is signed out
        authSection.classList.remove('hidden');
        header.classList.add('hidden');
        document.querySelectorAll('main > section.glass-panel').forEach(sec => sec.classList.add('hidden'));
    }
});

// Auth Toggle Logic
authToggleBtn.addEventListener('click', (e) => {
    e.preventDefault();
    isLoginMode = !isLoginMode;
    if (isLoginMode) {
        authSubmitBtn.textContent = 'Sign In';
        authToggleText.textContent = "Don't have an account?";
        authToggleBtn.textContent = "Sign Up";
    } else {
        authSubmitBtn.textContent = 'Create Account';
        authToggleText.textContent = "Already have an account?";
        authToggleBtn.textContent = "Sign In";
    }
    authError.classList.add('hidden');
});

// Login/Signup Handler
loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (isLoginMode) {
        signInWithEmailAndPassword(auth, email, password)
            .then(() => {
                loginForm.reset();
                authError.classList.add('hidden');
            })
            .catch((error) => {
                authError.textContent = error.message;
                authError.classList.remove('hidden');
            });
    } else {
        createUserWithEmailAndPassword(auth, email, password)
            .then((userCredential) => {
                // Also create user node in RTDB
                const uid = userCredential.user.uid;
                set(ref(db, 'users/' + uid), {
                    email: email,
                    name: email.split('@')[0],
                    bloodGroup: "Unknown",
                    location: "Unknown",
                    contact: "Unknown"
                }).then(() => {
                    loginForm.reset();
                    authError.classList.add('hidden');
                });
            })
            .catch((error) => {
                authError.textContent = error.message;
                authError.classList.remove('hidden');
            });
    }
});

// Logout Handler
logoutBtn.addEventListener('click', () => {
    signOut(auth);
});

// Load Dashboard Data (Realtime)
function loadDashboardData() {
    // Load Requests
    const requestsRef = ref(db, 'bloodRequests');
    onValue(requestsRef, (snapshot) => {
        urgentRequestsList.innerHTML = '';
        let count = 0;
        
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const request = childSnapshot.val();
                count++;
                
                if(request.urgency === 'Urgent' || request.urgency === 'Emergency') {
                    const item = document.createElement('div');
                    item.className = 'list-item';
                    item.innerHTML = `
                        <div>
                            <h4>${request.patientName} <span class="badge" style="background: var(--warning)">${request.bloodGroup}</span></h4>
                            <p><i class="ri-hospital-line"></i> ${request.hospital}</p>
                            <p style="color: var(--primary-color)"><i class="ri-alarm-warning-line"></i> ${request.urgency}</p>
                        </div>
                    `;
                    urgentRequestsList.appendChild(item);
                }
            });
        } else {
            urgentRequestsList.innerHTML = '<p style="color: var(--text-muted)">No urgent requests found.</p>';
        }
        recentRequestsCount.textContent = count;
    });

    // Load Donors
    const usersRef = ref(db, 'users');
    onValue(usersRef, (snapshot) => {
        if (snapshot.exists()) {
            availableDonorsCount.textContent = snapshot.size;
        } else {
            availableDonorsCount.textContent = "0";
        }
    });
}

// Load Nearby Data (Donors, Patients, Hospitals)
function loadNearbyData() {
    // Nearby Donors
    const usersRef = ref(db, 'users');
    onValue(usersRef, (snapshot) => {
        nearbyDonorsList.innerHTML = '';
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const user = childSnapshot.val();
                const item = document.createElement('div');
                item.className = 'list-item';
                item.innerHTML = `
                    <div>
                        <h4>${user.name || 'Anonymous Donor'} <span class="badge">${user.bloodGroup}</span></h4>
                        <p><i class="ri-phone-line"></i> ${user.contact || 'Not provided'}</p>
                        <p><i class="ri-map-pin-line"></i> ${user.location || 'Location shared'}</p>
                    </div>
                    <button class="primary-btn" style="width: auto; padding: 0.5rem 1rem;">Contact</button>
                `;
                nearbyDonorsList.appendChild(item);
            });
        } else {
            nearbyDonorsList.innerHTML = '<p style="color: var(--text-muted)">No donors found nearby.</p>';
        }
    });

    // Nearby Patients
    const requestsRef = ref(db, 'bloodRequests');
    onValue(requestsRef, (snapshot) => {
        nearbyPatientsList.innerHTML = '';
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const req = childSnapshot.val();
                const item = document.createElement('div');
                item.className = 'list-item';
                item.innerHTML = `
                    <div>
                        <h4>${req.patientName} <span class="badge" style="background: var(--primary-color)">${req.bloodGroup} Needed</span></h4>
                        <p><i class="ri-hospital-line"></i> ${req.hospital}</p>
                        <p style="color: var(--primary-color)"><i class="ri-alarm-warning-line"></i> ${req.urgency}</p>
                    </div>
                    <button class="primary-btn emergency-btn" style="width: auto; padding: 0.5rem 1rem;">Donate</button>
                `;
                nearbyPatientsList.appendChild(item);
            });
        } else {
            nearbyPatientsList.innerHTML = '<p style="color: var(--text-muted)">No active patient requests nearby.</p>';
        }
    });

    // Partnered Hospitals (Static list matching Android App)
    const hospitals = [
        { name: "Saveetha Medical College Hospital", loc: "Poonamallee, Chennai", status: "Available" },
        { name: "Apollo Speciality Hospital", loc: "Chennai", status: "Low Stock" },
        { name: "Medicover Hospital", loc: "Nellore", status: "Available" },
        { name: "City General Hospital", loc: "Main Road", status: "Available" }
    ];
    
    hospitalsList.innerHTML = '';
    hospitals.forEach(h => {
        const item = document.createElement('div');
        item.className = 'list-item';
        item.innerHTML = `
            <div>
                <h4>${h.name}</h4>
                <p><i class="ri-map-pin-line"></i> ${h.loc}</p>
                <p><i class="ri-drop-fill"></i> Blood Bank: <span style="color: ${h.status === 'Available' ? 'var(--success)' : 'var(--warning)'}">${h.status}</span></p>
            </div>
            <button class="primary-btn" style="width: auto; padding: 0.5rem 1rem; background: var(--success)">Directions</button>
        `;
        hospitalsList.appendChild(item);
    });
}

// Handle Blood Search
searchBtn.addEventListener('click', () => {
    const group = searchBloodGroup.value;
    const usersRef = ref(db, 'users');
    
    searchResults.innerHTML = '<p>Searching...</p>';
    
    onValue(usersRef, (snapshot) => {
        searchResults.innerHTML = '';
        let found = false;
        
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const user = childSnapshot.val();
                if (!group || user.bloodGroup === group) {
                    found = true;
                    const item = document.createElement('div');
                    item.className = 'list-item';
                    item.innerHTML = `
                        <div>
                            <h4>${user.name || 'Anonymous Donor'} <span class="badge">${user.bloodGroup}</span></h4>
                            <p><i class="ri-phone-line"></i> ${user.contact || 'Not provided'}</p>
                            <p><i class="ri-map-pin-line"></i> ${user.location || 'Unknown'}</p>
                        </div>
                        <button class="primary-btn" style="width: auto; padding: 0.5rem 1rem;">Contact</button>
                    `;
                    searchResults.appendChild(item);
                }
            });
        }
        
        if (!found) {
            searchResults.innerHTML = '<p style="color: var(--text-muted)">No donors found matching criteria.</p>';
        }
    }, { onlyOnce: true });
});

// Handle Emergency Request Submission
bloodRequestForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const patientName = document.getElementById('patient-name').value;
    const hospital = document.getElementById('hospital-name').value;
    const bloodGroup = document.getElementById('request-blood-group').value;
    const urgency = document.getElementById('urgency-level').value;
    const requesterId = auth.currentUser ? auth.currentUser.uid : "unknown";

    const requestsRef = ref(db, 'bloodRequests');
    const newRequestRef = push(requestsRef);
    
    set(newRequestRef, {
        patientName,
        hospital,
        bloodGroup,
        urgency,
        requesterId,
        timestamp: Date.now()
    }).then(() => {
        bloodRequestForm.reset();
        requestStatus.textContent = "Emergency request broadcasted successfully!";
        requestStatus.style.color = "var(--success)";
        requestStatus.classList.remove('hidden');
        
        setTimeout(() => {
            requestStatus.classList.add('hidden');
            switchSection('dashboard-section');
        }, 3000);
    }).catch(error => {
        requestStatus.textContent = error.message;
        requestStatus.style.color = "var(--primary-color)";
        requestStatus.classList.remove('hidden');
    });
});

// Handle Donor Registration Form Submission
const donorForm = document.getElementById('become-donor-form');
if (donorForm) {
    donorForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const name = document.getElementById('donor-name').value;
        const bloodGroup = document.getElementById('donor-blood-group').value;
        const phone = document.getElementById('donor-phone').value;
        const location = document.getElementById('donor-location').value;
        const age = document.getElementById('donor-age').value;
        const weight = document.getElementById('donor-weight').value;
        const lastDate = document.getElementById('donor-last-date').value;
        const diseases = document.getElementById('donor-diseases').checked;
        
        if (!diseases) {
            alert("You must confirm you have no severe underlying diseases to register as a donor.");
            return;
        }

        const donorId = auth.currentUser ? auth.currentUser.uid : Date.now().toString();
        const userRef = ref(db, 'users/' + donorId);
        
        set(userRef, {
            name,
            bloodGroup,
            contact: phone,
            location,
            age,
            weight,
            lastDonationDate: lastDate,
            isDonor: true,
            status: "Available"
        }).then(() => {
            donorForm.reset();
            const msg = document.getElementById('request-status-msg');
            if(msg) {
                msg.textContent = "Successfully registered as a donor!";
                msg.style.color = "var(--success)";
                msg.classList.remove('hidden');
                
                setTimeout(() => {
                    msg.classList.add('hidden');
                    switchSection('nearby-donors-section');
                }, 3000);
            }
        });
    });
}
