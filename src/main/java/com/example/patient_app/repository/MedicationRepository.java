package com.example.patient_app.repository;


import com.example.patient_app.model.Medication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends MongoRepository<Medication, String> {
    // Additional query methods if needed
}
