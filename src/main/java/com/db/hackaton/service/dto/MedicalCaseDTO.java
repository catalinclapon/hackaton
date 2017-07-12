package com.db.hackaton.service.dto;

import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MedicalCaseDTO {
    private Long id;

    private String uuid;

    private String registryUuid;

    private PatientDTO patient;

    private String status;

    private List<MedicalCaseFieldDTO> fields;

    public static MedicalCaseDTO build(MedicalCase mc) {
        return builder()
            .id(mc.getId())
            .fields(Optional.ofNullable(mc.getFields())
                .orElse(Collections.emptySet()).stream()
                .map(MedicalCaseFieldDTO::build)
                .collect(Collectors.toList()))
            .uuid(mc.getUuid())
            .status(mc.getStatus())
            .registryUuid(mc.getRegistryUuid())
            .patient(Optional.ofNullable(mc.getPatient())
                .map(PatientDTO::build)
                .orElse(null))
            .build();
    }

    public static MedicalCase build(MedicalCaseDTO mcDTO) {
        return new MedicalCase()
            .id(mcDTO.getId())
            .fields(Optional.ofNullable(mcDTO.getFields())
                .orElse(Collections.emptyList())
                .stream()
                .map(MedicalCaseFieldDTO::build)
                .collect(Collectors.toSet()))
            .patient(Optional.ofNullable(mcDTO.getPatient())
                .map(PatientDTO::build)
                .orElse(null))
            .uuid(mcDTO.getUuid())
            .registryUuid(mcDTO.getRegistryUuid())
            .status(mcDTO.getStatus());
    }
}
