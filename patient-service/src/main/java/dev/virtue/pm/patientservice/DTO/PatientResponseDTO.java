package dev.virtue.pm.patientservice.DTO;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO{
    private String id;
   private String name;
   private String email;
   private String address;
   private String dateOfBirth;
}