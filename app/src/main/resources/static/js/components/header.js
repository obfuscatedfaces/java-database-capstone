/**
 * header.js
 * Dynamically renders the navigation bar based on the user's role and login status.
 */

document.addEventListener("DOMContentLoaded", () => {
    renderHeader();
});

function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;


    if (window.location.pathname.endsWith("/") || window.location.pathname.endsWith("index.html")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");


    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    let headerContent = `<div class="nav-container">
                            <h1 onclick="window.location.href='/'">HealthCare Portal</h1>
                            <nav id="nav-links">`;


    if (role === "admin") {
        headerContent += `
            <button id="addDocBtn" class="adminBtn">Add Doctor</button>
            <button class="logout-btn" onclick="logout()">Logout</button>`;
    }
    else if (role === "doctor") {
        headerContent += `
            <a href="/doctorDashboard">Home</a>
            <button class="logout-btn" onclick="logout()">Logout</button>`;
    }
    else if (role === "loggedPatient") {
        headerContent += `
            <a href="/patientDashboard">Home</a>
            <a href="/appointments">My Appointments</a>
            <button class="logout-btn" onclick="logoutPatient()">Logout</button>`;
    }
    else if (role === "patient" || !role) {

        headerContent += `
            <button id="loginBtn" class="nav-btn">Login</button>
            <button id="signupBtn" class="nav-btn">Sign Up</button>`;
    }

    headerContent += `</nav></div>`;


    headerDiv.innerHTML = headerContent;


    attachHeaderButtonListeners();
}


function attachHeaderButtonListeners() {
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {

            if (typeof openModal === "function") openModal('addDoctor');
        });
    }

    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
             window.location.href = "/pages/login.html";
        });
    }
}


window.logout = function() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
};

window.logoutPatient = function() {
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient"); // Revert to guest patient status
    window.location.href = "/pages/patientDashboard.html";
};