package com.example.patient_app.repository;

import com.example.patient_app.model.Appointment;
import com.example.patient_app.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    boolean existsByPatientAndDoctorNameAndAppointmentDateTime(Patient patient, String doctorName, LocalDateTime appointmentDateTime);
}

