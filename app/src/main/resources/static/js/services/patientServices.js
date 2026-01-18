
import { API_BASE_URL } from "../config/config.js";

const PATIENT_API = API_BASE_URL + '/patient';


export async function patientSignup(data) {
    try {
        const response = await fetch(`${PATIENT_API}/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || (response.ok ? "Signup successful!" : "Signup failed")
        };
    } catch (error) {
        console.error("Signup Error:", error);
        return { success: false, message: "Server unreachable during signup." };
    }
}


export async function patientLogin(data) {
    try {

        console.log("Attempting login for:", data.email);

        const response = await fetch(`${PATIENT_API}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        return response;
    } catch (error) {
        console.error("Login Error:", error);
        throw error;
    }
}


export async function getPatientData(token) {
    try {
        const response = await fetch(`${PATIENT_API}/me?token=${token}`);
        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error("Error fetching patient data:", error);
        return null;
    }
}

/**
 * Fetch appointments for a specific user (Patient or Doctor).
 * @param {string} id - The user ID.
 * @param {string} token - Auth token.
 * @param {string} user - "patient" or "doctor"
 */
export async function getPatientAppointments(id, token, user) {
    const url = `${API_BASE_URL}/appointments/${user}/${id}?token=${token}`;

    try {
        const response = await fetch(url);
        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error("Error fetching appointments:", error);
        return null;
    }
}

export async function filterAppointments(condition, name, token) {
    const searchCondition = condition || "all";
    const searchName = name || "null";
    const url = `${API_BASE_URL}/appointments/filter/${searchCondition}/${searchName}?token=${token}`;

    try {
        const response = await fetch(url);
        if (response.ok) {
            return await response.json();
        }
        return [];
    } catch (error) {
        console.error("Filter Error:", error);
        alert("Could not filter appointments at this time.");
        return [];
    }
}