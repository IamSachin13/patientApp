package com.example.patient_app.controller;

import com.example.patient_app.model.Appointment;
import com.example.patient_app.model.Patient;
import com.example.patient_app.repository.AppointmentRepository;
import com.example.patient_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "appointments";
    }

    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam String patientId,
                                  @RequestParam String doctorName,
                                  @RequestParam String dateTime,
                                  Model model) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            model.addAttribute("error", "Patient not found.");
            model.addAttribute("appointments", appointmentRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "appointments";
        }

        LocalDateTime appointmentDateTime;
        try {
            appointmentDateTime = LocalDateTime.parse(dateTime);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date and time format.");
            model.addAttribute("appointments", appointmentRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "appointments";
        }

        // Check if a duplicate appointment exists
        boolean duplicateExists = appointmentRepository.existsByPatientAndDoctorNameAndAppointmentDateTime(
                patient, doctorName, appointmentDateTime);
        if (duplicateExists) {
            model.addAttribute("error", "An appointment with the same details already exists. Please choose a different time or doctor.");
            model.addAttribute("appointments", appointmentRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "appointments";
        }

        // Create and save the appointment if no duplicate exists
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctorName(doctorName);
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointmentRepository.save(appointment);

        model.addAttribute("message", "Appointment booked successfully!");
        model.addAttribute("appointments", appointmentRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "appointments";
    }

    // Delete an appointment
    @GetMapping("/appointments/delete/{id}")
    public String deleteAppointment(@PathVariable String id, Model model) {
        appointmentRepository.deleteById(id);
        // Using redirect ensures the appointment list is refreshed
        return "redirect:/appointments";
    }

    // Show the edit form for an appointment
    @GetMapping("/appointments/edit/{id}")
    public String showEditAppointment(@PathVariable String id, Model model) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            model.addAttribute("error", "Appointment not found.");
            return "redirect:/appointments";
        }
        model.addAttribute("appointment", appointment);
        model.addAttribute("patients", patientRepository.findAll());
        return "edit-appointment";
    }


    // Process the update of an appointment
    @PostMapping("/appointments/update")
    public String updateAppointment(@ModelAttribute Appointment appointment, Model model) {
        // Log or debug appointment.getId() to ensure it's not null
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);
        if (existingAppointment == null) {
            model.addAttribute("error", "Appointment not found.");
            return "redirect:/appointments";
        }
        existingAppointment.setDoctorName(appointment.getDoctorName());
        existingAppointment.setAppointmentDateTime(appointment.getAppointmentDateTime());
        existingAppointment.setPatient(appointment.getPatient());
        appointmentRepository.save(existingAppointment);
        return "redirect:/appointments";
    }
}
