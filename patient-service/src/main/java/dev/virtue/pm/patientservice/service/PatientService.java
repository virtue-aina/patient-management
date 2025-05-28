package dev.virtue.pm.patientservice.service;

import dev.virtue.pm.patientservice.DTO.PatientRequestDTO;
import dev.virtue.pm.patientservice.DTO.PatientResponseDTO;
import dev.virtue.pm.patientservice.exception.EmailAlreadyExistsException;
import dev.virtue.pm.patientservice.exception.PatientNotFoundException;
import dev.virtue.pm.patientservice.mapper.PatientMapper;
import dev.virtue.pm.patientservice.model.Patient;
import dev.virtue.pm.patientservice.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    /**
     * Get patients with pagination support
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @return List of patient DTOs for the requested page
     */
    public List<PatientResponseDTO> getPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        return patientPage.getContent().stream()
                .map(PatientMapper::toDTO)
                .toList();
    }
   public  PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
       Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

       if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
           throw new EmailAlreadyExistsException("Email address already in use by another patient");
       }

       return PatientMapper.toDTO(newPatient);

   }
    public  PatientResponseDTO updatePatient(UUID id,PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () ->new PatientNotFoundException("Patient with ID: " + id + "not found"  ));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlreadyExistsException("Email address already in use by another patient");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public  void deletePatient(UUID id) {
       patientRepository.deleteById(id);
    }

    /**
     * Search for patients by a search term
     * Searches in name, email, and address fields
     * 
     * @param searchTerm The term to search for
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @return List of patient DTOs matching the search criteria
     */
    public List<PatientResponseDTO> searchPatients(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.searchPatients(searchTerm, pageable);

        return patientPage.getContent().stream()
                .map(PatientMapper::toDTO)
                .toList();
    }




}
