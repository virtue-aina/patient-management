package dev.virtue.pm.patientservice.DTO;

import dev.virtue.pm.patientservice.DTO.validators.PatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientRequestDTO {
    @NotBlank(message="Name is required")
    @Size(max = 100, message = "Name cannot exceed more than 100 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email format")
    private String email;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "D.O.B is required")
    private String dateOfBirth;
    @NotBlank(groups = PatientValidationGroup.class,message = "Registration date is required")
    private String registeredDate;
}
