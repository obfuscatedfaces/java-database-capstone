package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
            return appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByCondition(Long patientId, String condition) {
        int status = parseCondition(condition);
        if (status == -1) return List.of();

        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatusOrderByAppointmentTimeAsc(patientId, status);
        return appointments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctor(Long patientId, String doctorName) {
        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
        return appointments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
        int status = parseCondition(condition);
        if (status == -1) return List.of();

        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
        return appointments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientDetails(String token) {
        String email = tokenService.getEmailFromToken(token);
        return patientRepository.findByEmail(email);
    }

    private int parseCondition(String condition) {
        if ("future".equalsIgnoreCase(condition)) return 0;
        if ("past".equalsIgnoreCase(condition)) return 1;
        return -1;
    }

    private AppointmentDTO convertToDTO(Appointment a) {
        return new AppointmentDTO(
                a.getId(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getPatient().getEmail(),
                a.getPatient().getPhone(),
                a.getPatient().getAddress(),
                a.getAppointmentTime(),
                a.getStatus()
        );
    }
}