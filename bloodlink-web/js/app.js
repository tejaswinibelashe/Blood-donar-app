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
window.switchSection = switchSection; // Export globally for HTML onclick handlers

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
        header.classList.add('hidden');
        document.querySelectorAll('main > section.glass-panel').forEach(sec => {
            if (sec.id !== 'auth-section') {
                sec.classList.add('hidden');
            }
        });
        authSection.classList.remove('hidden');
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

// Mock Data
const mockDonors = [
    { name: 'John Doe', bloodGroup: 'O+', location: 'Downtown Medical Center', distance: '1.2 km', age: 28, gender: 'Male', status: 'Available', lastDonation: '3 months ago' },
    { name: 'Jane Smith', bloodGroup: 'A-', location: 'Westside Clinic', distance: '3.5 km', age: 34, gender: 'Female', status: 'Available', lastDonation: '6 months ago' },
    { name: 'Mike Johnson', bloodGroup: 'B+', location: 'North Hospital', distance: '5.0 km', age: 41, gender: 'Male', status: 'Unavailable (Recently Donated)', lastDonation: '2 weeks ago' },
    { name: 'Emily Davis', bloodGroup: 'AB+', location: 'City Center', distance: '0.8 km', age: 25, gender: 'Female', status: 'Available', lastDonation: '1 year ago' },
    { name: 'Robert Wilson', bloodGroup: 'O-', location: 'South Medical', distance: '4.2 km', age: 45, gender: 'Male', status: 'Available', lastDonation: '4 months ago' }
];

const mockPatients = [
    { name: 'Sarah Connor', hospital: 'Downtown Medical Center', groupRequired: 'O-', urgency: 'Emergency', time: '10 mins ago', distance: '1.2 km' },
    { name: 'James Gordon', hospital: 'General Hospital', groupRequired: 'A+', urgency: 'Urgent', time: '1 hour ago', distance: '2.5 km' },
    { name: 'Bruce Wayne', hospital: 'Gotham Memorial', groupRequired: 'AB-', urgency: 'Normal', time: '3 hours ago', distance: '5.8 km' },
    { name: 'Clark Kent', hospital: 'Metropolis General', groupRequired: 'O+', urgency: 'Emergency', time: '5 mins ago', distance: '0.5 km' }
];

const mockHospitals = [
    { name: 'Downtown Medical Center', type: 'Level 1 Trauma Center', inventoryStatus: 'Critical - O Negative Shortage', location: '123 Main St, Downtown', phone: '(555) 123-4567' },
    { name: 'General Hospital', type: 'General Medical', inventoryStatus: 'Stable', location: '456 Oak Ave, Westside', phone: '(555) 987-6543' },
    { name: 'City Center Blood Bank', type: 'Dedicated Blood Bank', inventoryStatus: 'Good - All Types Available', location: '789 Pine Blvd, Central', phone: '(555) 555-0000' }
];


// Load Dashboard Data (Realtime)
function loadDashboardData() {
    // Basic Stats
    document.getElementById('stat-donors').textContent = mockDonors.length + 152;
    document.getElementById('stat-hospitals').textContent = mockHospitals.length + 28;
    document.getElementById('stat-patients').textContent = mockPatients.length + 84;
    document.getElementById('stat-emergencies').textContent = mockPatients.filter(p => p.urgency === 'Emergency').length;

    // Blood Group Availability Grid
    const bloodGrid = document.getElementById('dashboard-blood-grid');
    if (bloodGrid) {
        const groups = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];
        bloodGrid.innerHTML = groups.map(g => `
            <div class="blood-card">
                <h3>${g}</h3>
                <p style="font-size: 0.8rem; color: var(--text-muted)">${Math.floor(Math.random() * 50) + 5} Units</p>
            </div>
        `).join('');
    }

    // Recent Activities (Mix of real and mock)
    const requestsRef = ref(db, 'bloodRequests');
    onValue(requestsRef, (snapshot) => {
        if (!urgentRequestsList) return;
        urgentRequestsList.innerHTML = '';
        let items = '';
        if (snapshot.exists()) {
            snapshot.forEach((child) => {
                const req = child.val();
                items += `
                    <div class="list-item" style="border-left-color: #E63946;">
                        <div>
                            <h4>${req.patientName || 'Emergency'} <span class="badge badge-emergency">Real-time</span></h4>
                            <p>Needs ${req.bloodGroup} at ${req.hospital}</p>
                        </div>
                        <button class="btn btn-fill" style="width:auto; padding: 0.5rem 1rem">Respond</button>
                    </div>
                `;
            });
        }
        
        // Append mock recent activities
        items += mockPatients.slice(0, 3).map(p => `
            <div class="list-item" style="border-left-color: ${p.urgency === 'Emergency' ? '#E63946' : '#E9C46A'};">
                <div>
                    <h4>${p.name} <span class="badge badge-${p.urgency.toLowerCase()}">${p.urgency}</span></h4>
                    <p>Needs ${p.groupRequired} at ${p.hospital}</p>
                </div>
                <button class="btn btn-outline" style="width:auto; padding: 0.5rem 1rem" onclick="switchSection('nearby-patients-section')">View</button>
            </div>
        `).join('');
        
        urgentRequestsList.innerHTML = items || '<p>No recent activities.</p>';
    }, { onlyOnce: true });
}

function renderDonors(filterName = '', filterGroup = '') {
    if (!nearbyDonorsList) return;
    nearbyDonorsList.innerHTML = '';
    const allDonors = [...(window.currentDynamicDonors || []), ...mockDonors];
    const filtered = allDonors.filter(d => {
        const matchName = d.name.toLowerCase().includes(filterName.toLowerCase()) || d.location.toLowerCase().includes(filterName.toLowerCase());
        const matchGroup = filterGroup === '' || d.bloodGroup === filterGroup;
        return matchName && matchGroup;
    });

    if (filtered.length === 0) {
        nearbyDonorsList.innerHTML = '<p>No donors found matching criteria.</p>';
        return;
    }

    filtered.forEach(d => {
        nearbyDonorsList.innerHTML += `
            <div class="profile-card">
                <div class="profile-header">
                    <div class="avatar">${d.name.charAt(0)}</div>
                    <div class="profile-info">
                        <h3>${d.name} <span class="badge badge-normal">${d.bloodGroup}</span></h3>
                        <p><i class="ri-map-pin-line"></i> ${d.location} (${d.distance})</p>
                    </div>
                </div>
                <div class="profile-details">
                    <div><span style="color:var(--text-muted)">Age:</span> ${d.age}</div>
                    <div><span style="color:var(--text-muted)">Gender:</span> ${d.gender}</div>
                    <div><span style="color:var(--text-muted)">Status:</span> <span style="color:${d.status === 'Available' ? 'var(--success)' : 'var(--text-muted)'}">${d.status}</span></div>
                    <div><span style="color:var(--text-muted)">Last Donated:</span> ${d.lastDonation}</div>
                </div>
                <div class="profile-actions">
                    <button class="btn btn-outline"><i class="ri-user-line"></i> Profile</button>
                    <button class="btn btn-fill" ${d.status !== 'Available' ? 'disabled style="opacity:0.5"' : ''}><i class="ri-phone-line"></i> Contact</button>
                </div>
            </div>
        `;
    });
}

function renderPatients(filterName = '', filterUrgency = '') {
    nearbyPatientsList.innerHTML = '';
    const filtered = mockPatients.filter(p => {
        const matchName = p.name.toLowerCase().includes(filterName.toLowerCase()) || p.hospital.toLowerCase().includes(filterName.toLowerCase());
        const matchUrgency = filterUrgency === '' || p.urgency === filterUrgency;
        return matchName && matchUrgency;
    });

    filtered.forEach(p => {
        const badgeClass = p.urgency === 'Emergency' ? 'badge-emergency' : p.urgency === 'Urgent' ? 'badge-urgent' : 'badge-normal';
        nearbyPatientsList.innerHTML += `
            <div class="patient-card ${p.urgency.toLowerCase()}">
                <div style="flex: 1; min-width: 250px;">
                    <div style="display:flex; align-items:center; gap:10px; margin-bottom:5px;">
                        <h3 style="margin:0">${p.name}</h3>
                        <span class="badge ${badgeClass}">${p.urgency}</span>
                        <span class="badge" style="background:#333">${p.groupRequired}</span>
                    </div>
                    <p style="color: var(--text-muted); font-size:0.9rem;">
                        <i class="ri-hospital-line"></i> ${p.hospital} (${p.distance})
                    </p>
                    <p style="font-size:0.9rem; margin-top:5px;">Required: <strong>${p.units} Units</strong> | Status: ${p.status}</p>
                </div>
                <div style="display:flex; gap:10px;">
                    <button class="btn btn-outline" style="padding: 0.5rem 1rem; border-radius:6px; border:1px solid #555; background:transparent; color:white;"><i class="ri-share-line"></i> Share</button>
                    <button class="btn btn-fill" style="padding: 0.5rem 1rem; border-radius:6px; border:none; background:var(--primary-color); color:white;"><i class="ri-hand-heart-line"></i> Donate</button>
                </div>
            </div>
        `;
    });
}

function renderHospitals(filterName = '') {
    hospitalsList.innerHTML = '';
    const filtered = mockHospitals.filter(h => h.name.toLowerCase().includes(filterName.toLowerCase()) || h.location.toLowerCase().includes(filterName.toLowerCase()));

    filtered.forEach(h => {
        hospitalsList.innerHTML += `
            <div class="hospital-card">
                <img src="${h.img}" class="hospital-img" alt="${h.name}">
                <div class="hospital-content">
                    <h3 style="margin-bottom: 5px;">${h.name}</h3>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 15px;"><i class="ri-map-pin-line"></i> ${h.location}</p>
                    <div style="display: flex; justify-content: space-between; font-size: 0.9rem; margin-bottom: 15px;">
                        <span>Blood Bank: <strong style="color:${h.bloodBankStatus === 'Adequate' ? 'var(--success)' : h.bloodBankStatus === 'Critical' ? 'var(--primary-color)' : 'var(--warning)'}">${h.bloodBankStatus}</strong></span>
                    </div>
                    <button class="btn btn-outline" style="width: 100%; padding: 0.5rem; border-radius: 6px; border: 1px solid #555; background: transparent; color: white;"><i class="ri-phone-line"></i> ${h.phone}</button>
                </div>
            </div>
        `;
    });
}

// Search Listeners
document.getElementById('donor-search')?.addEventListener('input', (e) => renderDonors(e.target.value, document.getElementById('donor-filter-group').value));
document.getElementById('donor-filter-group')?.addEventListener('change', (e) => renderDonors(document.getElementById('donor-search').value, e.target.value));

document.getElementById('patient-search')?.addEventListener('input', (e) => renderPatients(e.target.value, document.getElementById('patient-filter-urgency').value));
document.getElementById('patient-filter-urgency')?.addEventListener('change', (e) => renderPatients(document.getElementById('patient-search').value, e.target.value));

document.getElementById('hospital-search')?.addEventListener('input', (e) => renderHospitals(e.target.value));

function loadNearbyData() {
    const usersRef = ref(db, 'users');
    onValue(usersRef, (snapshot) => {
        let dynamicDonors = [];
        if (snapshot.exists()) {
            snapshot.forEach((child) => {
                dynamicDonors.push(child.val());
            });
        }
        window.currentDynamicDonors = dynamicDonors;
        renderDonors(document.getElementById('donor-search')?.value || '', document.getElementById('donor-filter-group')?.value || '');
        
        // Also update the total donor count on the dashboard
        const donorStat = document.getElementById('stat-donors');
        if (donorStat) {
            donorStat.textContent = mockDonors.length + dynamicDonors.length + 152;
        }
    });

    renderPatients();
    renderHospitals();
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
    const submitBtn = bloodRequestForm.querySelector('button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Broadcasting...';
    submitBtn.disabled = true;

    try {
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
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            requestStatus.textContent = "Emergency request broadcasted successfully!";
            requestStatus.style.color = "var(--success)";
            requestStatus.classList.remove('hidden');
            
            setTimeout(() => {
                requestStatus.classList.add('hidden');
                switchSection('dashboard-section');
            }, 3000);
        }).catch(error => {
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            requestStatus.textContent = "Firebase Error: " + error.message;
            requestStatus.style.color = "var(--primary-color)";
            requestStatus.classList.remove('hidden');
        });
    } catch(err) {
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
        requestStatus.textContent = "JS Error: " + err.message;
        requestStatus.style.color = "var(--primary-color)";
        requestStatus.classList.remove('hidden');
    }
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
