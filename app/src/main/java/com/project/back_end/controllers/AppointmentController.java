package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {


        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "doctor");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }


        return ResponseEntity.ok(appointmentService.getAppointments(null, date.atStartOfDay(), date.atTime(23, 59), patientName));
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment, @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "patient");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }


        int validationCode = service.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime().toLocalDate(),
                appointment.getAppointmentTime().toLocalTime()
        );

        if (validationCode == 1) {
            appointmentService.bookAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Appointment booked successfully.");
        } else if (validationCode == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Time slot already taken.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid doctor ID.");
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "patient");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }


        String result = appointmentService.updateAppointment(appointment.getId(), appointment, appointment.getPatient().getId());
        return result.equals("Success") ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "patient");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }


        boolean deleted = appointmentService.cancelAppointment(id, null);
        return deleted ? ResponseEntity.ok("Appointment canceled.") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cancellation failed.");
    }
}