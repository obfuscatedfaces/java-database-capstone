package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName; // Fixed: Changed from Long to String
    private Long patientID;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;


    public AppointmentDTO() {}


    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientID, String patientName,
                          String patientEmail, String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientID = patientID;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Standard Getters
    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public Long getPatientID() { return patientID; }
    public String getPatientName() { return patientName; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }
    public String getPatientAddress() { return patientAddress; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public int getStatus() { return status; }


    public LocalDate getAppointmentDate() {
        return (appointmentTime != null) ? appointmentTime.toLocalDate() : null;
    }

    public LocalTime getAppointmentTimeOnly() {
        return (appointmentTime != null) ? appointmentTime.toLocalTime() : null;
    }

    public LocalDateTime getEndTime() {
        return (appointmentTime != null) ? appointmentTime.plusHours(1) : null;
    }
}