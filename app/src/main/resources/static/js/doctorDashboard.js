

import { getPatientAppointments, filterAppointments } from "./patientServices.js";
import { renderPatientRow } from "../components/patientRows.js";

document.addEventListener("DOMContentLoaded", () => {
    loadDoctorDashboard();
});

async function loadDoctorDashboard() {
    const doctorId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");


    const appointments = await getPatientAppointments(doctorId, token, "doctor");
    updateTable(appointments);


    const searchBar = document.getElementById("searchBar");
    const dateFilter = document.getElementById("dateFilter");
    const todayBtn = document.getElementById("todayAppointmentsBtn");

    const handleFilter = async () => {
        const query = searchBar.value;
        const filtered = await filterAppointments("all", query, token);
        updateTable(filtered);
    };

    if (searchBar) searchBar.addEventListener("input", handleFilter);

    if (dateFilter) {
        dateFilter.addEventListener("change", async () => {

            const selectedDate = dateFilter.value;

            handleFilter();
        });
    }

    if (todayBtn) {
        todayBtn.addEventListener("click", () => {
            window.location.reload();
        });
    }
}


function updateTable(appointments) {
    const tableBody = document.getElementById("patientTableBody");
    const noRecordMsg = document.getElementById("noRecordMessage");

    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!appointments || appointments.length === 0) {
        noRecordMsg.style.display = "block";
        return;
    }

    noRecordMsg.style.display = "none";

    appointments.forEach(appp => {

        const row = renderPatientRow(appp);
        tableBody.appendChild(row);
    });
}


window.openPrescriptionModal = function(patientId) {

    if (typeof openModal === "function") {
        localStorage.setItem("activePatientId", patientId);
        openModal("addPrescription");
    }
};