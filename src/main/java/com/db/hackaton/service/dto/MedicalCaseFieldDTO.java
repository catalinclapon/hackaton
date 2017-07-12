package com.db.hackaton.service.dto;


import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.domain.RegistryField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MedicalCaseFieldDTO {
    private Long id;
    private FieldDTO field;
    private String value;

    public static MedicalCaseFieldDTO build(MedicalCaseField field) {
        return builder()
            .id(field.getId())
            .field(FieldDTO.build(field.getField()))
            .value(field.getValue())
            .build();
    }

    public static MedicalCaseField build(MedicalCaseFieldDTO fieldDTO) {
        return new MedicalCaseField()
            .id(fieldDTO.getId())
            .value(fieldDTO.getValue())
            .field(FieldDTO.build(fieldDTO.getField()));
    }
}
