
import { getDoctors, saveDoctor, filterDoctors, deleteDoctor as deleteDoctorService } from "./doctorServices.js";
import { createDoctorCard } from "../components/doctorCard.js";


document.addEventListener("DOMContentLoaded", () => {
    loadAdminDashboard();
});


async function loadAdminDashboard() {

    const doctors = await getDoctors();
    renderDoctorList(doctors);


    const searchBar = document.getElementById("searchBar");
    const filterTime = document.getElementById("filterTime");
    const filterSpecialty = document.getElementById("filterSpecialty");

    const triggerFilter = async () => {
        const filtered = await filterDoctors(
            searchBar.value,
            filterTime.value,
            filterSpecialty.value
        );
        renderDoctorList(filtered);
    };

    if (searchBar) searchBar.addEventListener("input", triggerFilter);
    if (filterTime) filterTime.addEventListener("change", triggerFilter);
    if (filterSpecialty) filterSpecialty.addEventListener("change", triggerFilter);
}


function renderDoctorList(doctors) {
    const contentArea = document.getElementById("content");
    if (!contentArea) return;

    contentArea.innerHTML = "";
    if (doctors.length === 0) {
        contentArea.innerHTML = "<p class='no-records'>No doctors found matching those criteria.</p>";
        return;
    }

    doctors.forEach(doc => {
        const card = createDoctorCard(doc);
        contentArea.appendChild(card);
    });
}


window.addDoctorHandler = async function() {
    const firstName = document.getElementById("newDocFirstName").value;
    const lastName = document.getElementById("newDocLastName").value;
    const email = document.getElementById("newDocEmail").value;
    const specialization = document.getElementById("newDocSpecialty").value;

    const token = localStorage.getItem("token");


    if (!firstName || !lastName || !email) {
        alert("Please fill in all required fields.");
        return;
    }

    const newDoctor = {
        firstName,
        lastName,
        email,
        specialization,
        availableTimes: ["9:00 AM", "2:00 PM"]
    };

    const result = await saveDoctor(newDoctor, token);

    if (result.success) {
        alert("Doctor added successfully!");
        window.location.reload();
    } else {
        alert("Error adding doctor: " + result.message);
    }
};


export async function deleteDoctor(id, token) {
    return await deleteDoctorService(id, token);
}