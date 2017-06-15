package com.db.hackaton.service.dto;

import com.db.hackaton.domain.Field;
import com.db.hackaton.domain.RegistryField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by adrian on 6/15/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistryFieldDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String category;

    private Integer order;

    private FieldDTO field;


    public static RegistryFieldDTO build(RegistryField field) {
        return builder()
            .id(field.getId())
            .category(field.getCategory())
            .order(field.getOrder())
            .field(FieldDTO.build(field.getField()))
            .build();
    }

    public static RegistryField build(RegistryFieldDTO fieldDTO) {
        return new RegistryField()
            .id(fieldDTO.getId())
            .category(fieldDTO.getCategory())
            .order(fieldDTO.getOrder())
            .field(FieldDTO.build(fieldDTO.getField()));
    }
}
