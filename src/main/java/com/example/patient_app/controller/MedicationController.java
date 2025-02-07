package com.example.patient_app.controller;


import com.example.patient_app.model.Medication;
import com.example.patient_app.model.Patient;
import com.example.patient_app.repository.MedicationRepository;
import com.example.patient_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MedicationController {

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/medications")
    public String listMedications(Model model) {
        model.addAttribute("medications", medicationRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "medications";
    }

    @PostMapping("/medications/add")
    public String addMedication(@RequestParam String patientId,
                                @RequestParam String name,
                                @RequestParam String dosage,
                                @RequestParam String instructions,
                                Model model) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            model.addAttribute("error", "Patient not found.");
            return "medications";
        }

        Medication medication = new Medication();
        medication.setPatient(patient);
        medication.setName(name);
        medication.setDosage(dosage);
        medication.setInstructions(instructions);

        medicationRepository.save(medication);
        model.addAttribute("message", "Medication added successfully!");
        model.addAttribute("medications", medicationRepository.findAll());
        return "medications";
    }
}
