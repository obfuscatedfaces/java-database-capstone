

import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + '/doctor';


export async function getDoctors() {
    try {
        const response = await fetch(DOCTOR_API);
        if (!response.ok) throw new Error("Failed to fetch doctors");
        return await response.json();
    } catch (error) {
        console.error("Error in getDoctors:", error);
        return [];
    }
}


export async function deleteDoctor(id, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${id}?token=${token}`, {
            method: 'DELETE'
        });

        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || (response.ok ? "Doctor deleted" : "Delete failed")
        };
    } catch (error) {
        console.error("Error in deleteDoctor:", error);
        return { success: false, message: "Server error during deletion." };
    }
}


export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}?token=${token}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(doctor)
        });

        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || "Doctor saved successfully"
        };
    } catch (error) {
        console.error("Error in saveDoctor:", error);
        return { success: false, message: "Failed to connect to server." };
    }
}


export async function filterDoctors(name, time, specialty) {

    const searchName = name || "null";
    const searchTime = time || "null";
    const searchSpec = specialty || "null";

    const filterUrl = `${DOCTOR_API}/filter/${searchName}/${searchTime}/${searchSpec}`;

    try {
        const response = await fetch(filterUrl);
        if (!response.ok) return [];
        return await response.json();
    } catch (error) {
        console.error("Error in filterDoctors:", error);
        return [];
    }
}