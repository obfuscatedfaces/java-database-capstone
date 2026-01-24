package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;

    public PrescriptionController(PrescriptionService prescriptionService,
                                  Service service,
                                  AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> savePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "doctor");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        appointmentService.changeStatus(prescription.getAppointmentId(), 1);

        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "doctor");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}