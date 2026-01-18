
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";


window.renderContent = async function () {
    const role = localStorage.getItem("userRole");


    const doctors = await getDoctors();
    displayDoctors(doctors);


    setupFilters();
};


function setupFilters() {
    const searchBar = document.getElementById("searchBar");
    const filterTime = document.getElementById("filterTime");
    const filterSpecialty = document.getElementById("filterSpecialty");

    const handleFilter = async () => {
        const name = searchBar.value;
        const time = filterTime.value;
        const specialty = filterSpecialty.value;


        const filteredDoctors = await filterDoctors(name, time, specialty);
        displayDoctors(filteredDoctors);
    };


    if (searchBar) searchBar.addEventListener("input", handleFilter);
    if (filterTime) filterTime.addEventListener("change", handleFilter);
    if (filterSpecialty) filterSpecialty.addEventListener("change", handleFilter);
}


function displayDoctors(doctorList) {
    const contentArea = document.getElementById("content");
    if (!contentArea) return;

    contentArea.innerHTML = "";

    if (doctorList.length === 0) {
        contentArea.innerHTML = `
            <div class="no-records">
                <p>No doctors found matching your criteria. Try adjusting your filters.</p>
            </div>
        `;
        return;
    }


    doctorList.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentArea.appendChild(card);
    });
}


window.confirmBooking = async function (event, doctorId) {
    event.preventDefault();

    const token = localStorage.getItem("token");
    const date = document.getElementById("appointmentDate").value;
    const time = document.getElementById("appointmentTime").value;

    const bookingData = {
        doctorId: doctorId,
        date: date,
        time: time
    };

    try {
        const response = await fetch('/api/appointments/book', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(bookingData)
        });

        if (response.ok) {
            alert("Appointment booked successfully!");
            document.getElementById("modal").style.display = "none";
        } else {
            alert("Booking failed. Please try again.");
        }
    } catch (error) {
        console.error("Booking Error:", error);
    }
};