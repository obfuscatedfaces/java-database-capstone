package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// REMOVED: import org.springframework.stereotype.Service; (This was causing the crash!)

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service // <--- FIX: Used full name here
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String requiredRole) {
        Map<String, String> errors = tokenService.validateToken(token, requiredRole);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> validateAdmin(String username, String password) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Admin> admin = adminRepository.findByUsername(username);
            if (admin.isPresent() && admin.get().getPassword().equals(password)) {
                // FIXED: Added username parameter
                String token = tokenService.generateToken(username, "ADMIN");
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("error", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Doctor> filterDoctor(String name, String specialty, String timePart) {
        if (name != null && specialty != null && timePart != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, timePart);
        } else if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        } else if (name != null && timePart != null) {
            return doctorService.filterDoctorByNameAndTime(name, timePart);
        } else if (specialty != null && timePart != null) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, timePart);
        } else if (name != null) {
            return doctorService.findDoctorByName(name);
        } else if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        } else if (timePart != null) {
            return doctorService.filterDoctorsByTime(timePart);
        }
        return doctorService.getDoctors();
    }

    public int validateAppointment(Long doctorId, LocalDate date, LocalTime requestedTime) {
        if (!doctorRepository.existsById(doctorId)) {
            return -1;
        }
        List<LocalTime> availableSlots = doctorService.getDoctorAvailability(doctorId, date);
        if (availableSlots.contains(requestedTime)) {
            return 1;
        }
        return 0;
    }

    public boolean validatePatient(String email, String phone) {
        return patientRepository.findByEmailOrPhone(email, phone).isEmpty();
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(String email, String password) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Patient> patient = patientRepository.findByEmail(email);
            if (patient.isPresent() && patient.get().getPassword().equals(password)) {
                String token = tokenService.generateToken(email, "PATIENT");
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<AppointmentDTO> filterPatient(String token, String condition, String doctorName) {
        String email = tokenService.getEmailFromToken(token);


        if (email == null) return List.of();

        Optional<Patient> patient = patientRepository.findByEmail(email);

        if (patient.isEmpty()) return List.of();
        Long patientId = patient.get().getId();

        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(patientId, doctorName, condition);
        } else if (condition != null) {
            return patientService.filterByCondition(patientId, condition);
        } else if (doctorName != null) {
            return patientService.filterByDoctor(patientId, doctorName);
        }
        return patientService.getPatientAppointment(patientId);
    }
}