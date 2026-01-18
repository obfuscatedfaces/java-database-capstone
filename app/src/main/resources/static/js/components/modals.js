

export function openModal(type) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");
    const closeModal = document.getElementById("closeModal");

    if (!modal || !modalBody) return;


    modalBody.innerHTML = "";


    if (type === 'adminLogin') {
        modalBody.innerHTML = `
            <div class="modal-form">
                <h3>Admin Secure Login</h3>
                <input type="text" id="adminUsername" placeholder="Username" class="input-field" />
                <input type="password" id="adminPassword" placeholder="Password" class="input-field" />
                <button class="doctor-btn" onclick="adminLoginHandler()">Login as Admin</button>
            </div>
        `;
    }
    else if (type === 'doctorLogin') {
        modalBody.innerHTML = `
            <div class="modal-form">
                <h3>Doctor Portal Login</h3>
                <input type="email" id="doctorEmail" placeholder="Doctor Email" class="input-field" />
                <input type="password" id="doctorPassword" placeholder="Password" class="input-field" />
                <button class="doctor-btn" onclick="doctorLoginHandler()">Login to Dashboard</button>
            </div>
        `;
    }
    else if (type === 'addDoctor') {
        modalBody.innerHTML = `
            <div class="modal-form">
                <h3>Register New Doctor</h3>
                <input type="text" id="newDocFirstName" placeholder="First Name" class="input-field" />
                <input type="text" id="newDocLastName" placeholder="Last Name" class="input-field" />
                <input type="email" id="newDocEmail" placeholder="Email Address" class="input-field" />
                <select id="newDocSpecialty" class="select-dropdown">
                    <option value="Cardiology">Cardiology</option>
                    <option value="Pediatrics">Pediatrics</option>
                    <option value="Neurology">Neurology</option>
                </select>
                <button class="doctor-btn" onclick="addDoctorHandler()">Add Doctor</button>
            </div>
        `;
    }


    modal.style.display = "flex";


    closeModal.onclick = () => {
        modal.style.display = "none";
    };

    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}


export function showBookingOverlay(event, doctor) {

    const ripple = document.createElement("div");
    ripple.classList.add("ripple-overlay");
    document.body.appendChild(ripple);


    ripple.style.left = `${event.clientX}px`;
    ripple.style.top = `${event.clientY}px`;
    ripple.classList.add("ripple-active");


    setTimeout(() => {
        ripple.remove();
        const modalBody = document.getElementById("modal-body");
        modalBody.innerHTML = `
            <div class="modalApp active">
                <h3>Book Appointment with Dr. ${doctor.lastName}</h3>
                <p>Specialty: ${doctor.specialization}</p>
                <form onsubmit="confirmBooking(event, ${doctor.id})">
                    <input type="date" required id="appointmentDate" />
                    <select id="appointmentTime" required>
                        ${doctor.availableTimes.map(time => `<option value="${time}">${time}</option>`).join('')}
                    </select>
                    <button type="submit" class="book-confirm-btn">Confirm Booking</button>
                </form>
            </div>
        `;
        document.getElementById("modal").style.display = "flex";
    }, 600);
}


window.openModal = openModal;