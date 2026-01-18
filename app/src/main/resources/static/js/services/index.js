

import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';


const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';


window.onload = function () {
    const adminBtn = document.getElementById('btn-admin');
    const doctorBtn = document.getElementById('btn-doctor');
    const patientBtn = document.getElementById('btn-patient');

    if (adminBtn) {
        adminBtn.addEventListener('click', () => {
            openModal('adminLogin');
        });
    }

    if (doctorBtn) {
        doctorBtn.addEventListener('click', () => {
            openModal('doctorLogin');
        });
    }


    if (patientBtn) {
        patientBtn.addEventListener('click', () => {

            if (typeof selectRole === "function") {
                selectRole('patient');
            } else {
                localStorage.setItem("userRole", "patient");
                window.location.href = "pages/patientDashboard.html";
            }
        });
    }
};


window.adminLoginHandler = async function () {
    const usernameInput = document.getElementById('adminUsername');
    const passwordInput = document.getElementById('adminPassword');

    if (!usernameInput || !passwordInput) return;

    const username = usernameInput.value;
    const password = passwordInput.value;

    const admin = { username, password };

    try {
        const response = await fetch(ADMIN_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(admin)
        });

        if (response.ok) {
            const data = await response.json();

            localStorage.setItem('token', data.token);


            selectRole('admin');
        } else {
            alert("Invalid Admin credentials!");
        }
    } catch (error) {
        console.error("Login Error:", error);
        alert("An error occurred during admin login. Please check your connection.");
    }
};


window.doctorLoginHandler = async function () {
    const emailInput = document.getElementById('doctorEmail');
    const passwordInput = document.getElementById('doctorPassword');

    if (!emailInput || !passwordInput) return;

    const email = emailInput.value;
    const password = passwordInput.value;

    const doctor = { email, password };

    try {
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor)
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);


            selectRole('doctor');
        } else {
            alert("Invalid Doctor credentials!");
        }
    } catch (error) {
        console.error("Login Error:", error);
        alert("An error occurred during doctor login.");
    }
};