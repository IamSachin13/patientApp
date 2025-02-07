package com.example.patient_app.repository;


import com.example.patient_app.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    // Additional query methods if needed
}
