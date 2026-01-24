package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, user);
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        return ResponseEntity.ok(doctorService.getDoctorAvailability(doctorId, date));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Doctor>>> getDoctor() {
        Map<String, List<Doctor>> response = new HashMap<>();
        response.put("doctors", doctorService.getDoctors());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "admin");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        int result = doctorService.saveDoctor(doctor);
        Map<String, String> response = new HashMap<>();
        if (result == -1) {
            response.put("message", "Doctor already exists.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else if (result == 1) {
            response.put("message", "Doctor added successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login loginDto) {
        String result = doctorService.validateDoctor(loginDto.getEmail(), loginDto.getPassword());
        Map<String, String> response = new HashMap<>();
        if (result.equals("Invalid Credentials")) {
            response.put("message", result);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        response.put("token", result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "admin");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        int result = doctorService.updateDoctor(doctor);
        Map<String, String> response = new HashMap<>();
        if (result == -1) {
            response.put("message", "Doctor not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("message", "Doctor updated successfully.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        ResponseEntity<Map<String, String>> authResponse = service.validateToken(token, "admin");
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            return authResponse;
        }

        int result = doctorService.deleteDoctor(id);
        Map<String, String> response = new HashMap<>();
        if (result == -1) {
            response.put("message", "Doctor not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("message", "Doctor deleted successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<List<Doctor>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        String filterName = "null".equals(name) ? null : name;
        String filterTime = "null".equals(time) ? null : time;
        String filterSpec = "null".equals(speciality) ? null : speciality;

        return ResponseEntity.ok(service.filterDoctor(filterName, filterSpec, filterTime));
    }
}