package com.db.hackaton.service.dto;

import com.db.hackaton.domain.Registry;
import com.db.hackaton.domain.User;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by adrian on 6/15/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistryDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    private String uuid;

    private String status;

    private Set<RegistryFieldDTO> fields = new HashSet<>();

    public static Registry build(RegistryDTO registry) {
        return new Registry()
            .id(registry.getId())
            .name(registry.getName())
            .desc(registry.getDescription())
            .status(registry.getStatus())
            .fields(registry.getFields().stream()
                .map(RegistryFieldDTO::build)
                .collect(Collectors.toSet())
            );
    }

    public static RegistryDTO build(Registry registry) {
        return builder()
            .description(registry.getDesc())
            .uuid(registry.getUuid())
            .name(registry.getName())
            .id(registry.getId())
            .fields(registry.getFields().stream()
                .map(RegistryFieldDTO::build)
                .collect(Collectors.toSet()))
            .build();
    }
}
