(function() {
    // Check if on auth pages
    const path = window.location.pathname;
    const isAuthPage = path.includes('login.html') || path.includes('register.html') || path.includes('forgot-password.html');
    const token = localStorage.getItem('token');
    
    if (!token && !isAuthPage) {
        window.location.replace('login.html');
    }
    
    if (token && isAuthPage) {
        window.location.replace('index.html');
    }

    document.addEventListener('DOMContentLoaded', () => {
        const logoutBtn = document.getElementById('btnLogout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                window.location.replace('login.html');
            });
        }
    });
})();
