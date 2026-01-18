
import { deleteDoctor } from "../services/adminDashboard.js";
import { showBookingOverlay } from "./modals.js";

export function createDoctorCard(doctor) {

    const card = document.createElement("div");
    card.classList.add("doctor-card");

    const role = localStorage.getItem("userRole");


    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = `Dr. ${doctor.lastName}`;

    const specialization = document.createElement("p");
    specialization.classList.add("specialization");
    specialization.textContent = doctor.specialization;

    const email = document.createElement("p");
    email.classList.add("email");
    email.textContent = doctor.email;

    const availability = document.createElement("p");
    availability.classList.add("availability");

    availability.textContent = Array.isArray(doctor.availableTimes)
        ? doctor.availableTimes.join(", ")
        : doctor.availableTimes;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);


    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");


    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete Doctor";
        removeBtn.classList.add("admin-delete-btn");

        removeBtn.addEventListener("click", async () => {
            if (confirm(`Are you sure you want to delete Dr. ${doctor.lastName}?`)) {
                const token = localStorage.getItem("token");
                const success = await deleteDoctor(doctor.id, token);
                if (success) {
                    card.remove();
                }
            }
        });
        actionsDiv.appendChild(removeBtn);

    } else if (role === "patient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.addEventListener("click", () => {
            alert("Please login as a patient to book an appointment.");
            window.location.href = "/pages/login.html";
        });
        actionsDiv.appendChild(bookNow);

    } else if (role === "loggedPatient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Appointment";
        bookNow.addEventListener("click", async (e) => {

            showBookingOverlay(e, doctor);
        });
        actionsDiv.appendChild(bookNow);
    }


    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}