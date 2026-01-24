package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;


    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public String updateAppointment(Long id, Appointment updatedData, Long patientId) {
        return appointmentRepository.findById(id).map(existing -> {
            if (!existing.getPatient().getId().equals(patientId)) {
                return "Unauthorized: Patient ID mismatch.";
            }

            existing.setAppointmentTime(updatedData.getAppointmentTime());
            appointmentRepository.save(existing);
            return "Success";
        }).orElse("Appointment not found.");
    }

    @Transactional
    public boolean cancelAppointment(Long appointmentId, Long patientId) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            if (appointment.getPatient().getId().equals(patientId)) {
                appointmentRepository.delete(appointment);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, LocalDateTime start, LocalDateTime end, String patientName) {
        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, start, end);
        }
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    }

    @Transactional
    public void changeStatus(Long id, int status) {
        appointmentRepository.updateStatus(status, id);
    }
}