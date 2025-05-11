package dev.virtue.pm.patientservice.controller;

import dev.virtue.pm.patientservice.DTO.PatientRequestDTO;
import dev.virtue.pm.patientservice.DTO.PatientResponseDTO;
import dev.virtue.pm.patientservice.DTO.validators.PatientValidationGroup;
import dev.virtue.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name ="Patient",description = "API for managing Patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
     List<PatientResponseDTO> patients = patientService.getPatients();
            return ResponseEntity.ok().body(patients);
    }
    @PostMapping
    @Operation(summary = "Add new Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, PatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){

            PatientResponseDTO patientResponseDTO =patientService.createPatient(patientRequestDTO);
            return ResponseEntity.ok().body(patientResponseDTO);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update a Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable UUID id,@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO){

    PatientResponseDTO patientResponseDTO =patientService.updatePatient(id,patientRequestDTO);
    return ResponseEntity.ok().body(patientResponseDTO);
    }
    @DeleteMapping("/{id}")
    @Operation(summary= "Delete a Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
      patientService.deletePatient(id);
      return ResponseEntity.noContent().build();
    }

}
