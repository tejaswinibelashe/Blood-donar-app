const App = {
    init: function() {
        this.bindEvents();
        this.initTooltips();
        this.checkAuth();
    },

    bindEvents: function() {
        // Sidebar Toggle
        const toggleBtn = document.getElementById('sidebarToggle');
        const sidebar = document.getElementById('sidebar');
        
        if (toggleBtn && sidebar) {
            toggleBtn.addEventListener('click', () => {
                sidebar.classList.toggle('show');
            });
        }

        // Mock Logout
        const logoutBtn = document.getElementById('btnLogout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                if(confirm("Are you sure you want to logout?")) {
                    localStorage.removeItem('bloodlink_user');
                    window.location.href = 'login.html';
                }
            });
        }
    },

    initTooltips: function() {
        // Initialize Bootstrap tooltips if available
        if (typeof bootstrap !== 'undefined') {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl)
            });
        }
    },

    checkAuth: function() {
        const publicPages = ['login.html', 'register.html', 'forgot-password.html'];
        const currentPage = window.location.pathname.split('/').pop();
        
        // Mock session check
        const user = localStorage.getItem('bloodlink_user');
        
        if (!user && !publicPages.includes(currentPage) && currentPage !== '') {
            // Uncomment for strict auth check
            // window.location.href = 'login.html';
        }
    },

    // Geolocation Helper
    getLocation: function(callback, errorCallback) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const coords = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    callback(coords);
                },
                (error) => {
                    console.error("Error getting location", error);
                    if(errorCallback) errorCallback(error);
                },
                { enableHighAccuracy: true }
            );
        } else {
            console.error("Geolocation is not supported by this browser.");
            if(errorCallback) errorCallback(new Error("Not supported"));
        }
    },

    // Calculate Distance (Haversine formula)
    calculateDistance: function(lat1, lon1, lat2, lon2) {
        const R = 6371; // Radius of the earth in km
        const dLat = this.deg2rad(lat2 - lat1);
        const dLon = this.deg2rad(lon2 - lon1);
        const a = 
            Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
            Math.sin(dLon/2) * Math.sin(dLon/2); 
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        const d = R * c; // Distance in km
        return d.toFixed(1);
    },

    deg2rad: function(deg) {
        return deg * (Math.PI/180);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    App.init();
});
