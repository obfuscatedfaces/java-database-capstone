
export function renderPatientRow(appointment) {
    const tr = document.createElement("tr");

    tr.innerHTML = `
        <td>${appointment.patientId}</td>
        <td>${appointment.patientName}</td>
        <td>${appointment.patientPhone}</td>
        <td>${appointment.patientEmail}</td>
        <td>
            <button class="prescription-btn" onclick="openPrescriptionModal(${appointment.patientId})">
                + Add Prescription
            </button>
        </td>
    `;

    return tr;
}