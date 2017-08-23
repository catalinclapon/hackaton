package com.db.hackaton.service.dto;

import com.db.hackaton.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PatientDTO {
    private Long id;

    private Integer flag;

    private String cnp;

    public static PatientDTO build(Patient patient) {
        return builder()
            .cnp(patient.getCnp())
            .id(patient.getId())
            .flag(1)
            .build();
    }

    public static Patient build(PatientDTO patientDTO) {
        return new Patient()
            .cnp(patientDTO.getCnp())
            .id(patientDTO.getId());
    }
}
