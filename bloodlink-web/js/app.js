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

const navBtns = document.querySelectorAll('.nav-btn:not(.danger)');

// Sections mapping
const sections = {
    'dashboard-section': dashboardSection,
    'search-section': searchSection,
    'request-section': requestSection
};

// Navigation Logic
function switchSection(targetId) {
    Object.values(sections).forEach(sec => sec.classList.add('hidden'));
    sections[targetId].classList.remove('hidden');

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

// Authentication State Observer
onAuthStateChanged(auth, (user) => {
    if (user) {
        // User is signed in
        authSection.classList.add('hidden');
        header.classList.remove('hidden');
        dashboardSection.classList.remove('hidden');
        
        // Use email prefix as display name for simplicity
        const displayName = user.email.split('@')[0];
        userDisplayName.textContent = displayName;
        
        loadDashboardData();
    } else {
        // User is signed out
        authSection.classList.remove('hidden');
        header.classList.add('hidden');
        Object.values(sections).forEach(sec => sec.classList.add('hidden'));
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
