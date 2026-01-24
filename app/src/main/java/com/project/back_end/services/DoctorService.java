package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<LocalTime> getDoctorAvailability(Long doctorId, LocalDate date) {
        List<LocalTime> allSlots = new ArrayList<>();
        for (int hour = 9; hour < 17; hour++) {
            allSlots.add(LocalTime.of(hour, 0));
        }

        List<Appointment> booked = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        List<LocalTime> bookedTimes = booked.stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toList());

        allSlots.removeAll(bookedTimes);
        return allSlots;
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) {
            return -1;
        }
        doctorRepository.save(doctor);
        return 1;
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public String validateDoctor(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isPresent() && doctorOpt.get().getPassword().equals(password)) {
            return tokenService.generateToken(email, "DOCTOR");
        }
        return "Invalid Credentials";
    }

    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike(name);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String timePart) {
        List<Doctor> list = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterByTimePart(list, timePart);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String timePart) {
        return filterByTimePart(doctors, timePart);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndTime(String name, String timePart) {
        List<Doctor> list = doctorRepository.findByNameLike(name);
        return filterByTimePart(list, timePart);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String timePart) {
        List<Doctor> list = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterByTimePart(list, timePart);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByTime(String timePart) {
        return filterByTimePart(doctorRepository.findAll(), timePart);
    }

    private List<Doctor> filterByTimePart(List<Doctor> doctors, String timePart) {
        return doctors.stream().filter(d -> {
            List<LocalTime> availability = getDoctorAvailability(d.getId(), LocalDate.now());
            if ("AM".equalsIgnoreCase(timePart)) {
                return availability.stream().anyMatch(t -> t.getHour() < 12);
            } else if ("PM".equalsIgnoreCase(timePart)) {
                return availability.stream().anyMatch(t -> t.getHour() >= 12);
            }
            return true;
        }).collect(Collectors.toList());
    }
}