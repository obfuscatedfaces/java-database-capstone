package com.project.back_end.controllers;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    @GetMapping("/details/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "patient");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        Optional<Patient> patient = patientService.getPatientDetails(token);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();

        boolean isValid = service.validatePatient(patient.getEmail(), patient.getPhone());
        if (!isValid) {
            response.put("message", "Patient with this email or phone already exists.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            response.put("message", "Registration successful.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("message", "Internal server error during registration.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login loginDto) {
        return service.validatePatientLogin(loginDto.getEmail(), loginDto.getPassword());
    }

    @GetMapping("/appointments/{patientId}/{role}/{token}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String role,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, role);
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        List<AppointmentDTO> appointments = patientService.getPatientAppointment(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "patient");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        String filterCondition = "null".equals(condition) ? null : condition;
        String filterName = "null".equals(name) ? null : name;

        List<AppointmentDTO> filtered = service.filterPatient(token, filterCondition, filterName);
        return ResponseEntity.ok(filtered);
    }
}