package com.db.hackaton.service.dto;

import com.db.hackaton.domain.Field;
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
public class FieldDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    private String required;

    private Integer min;

    private Integer max;

    private String extValidation;
    private String type;


    public static FieldDTO build(Field field) {
        return builder()
            .id(field.getId())
            .description(field.getDesc())
            .name(field.getName())
            .required(field.getRequired())
            .min(field.getMin())
            .max(field.getMax())
            .type(field.getType())
            .build();
    }

    public static Field build(FieldDTO fieldDTO) {
        return new Field()
            .name(fieldDTO.getName())
            .desc(fieldDTO.getDescription())
            .required(fieldDTO.getRequired())
            .extValidation(fieldDTO.getExtValidation())
            .min(fieldDTO.getMin())
            .max(fieldDTO.getMax())
            .extValidation(fieldDTO.getExtValidation())
            .type(fieldDTO.getType());
    }
}
