package com.db.hackaton.service.dto;

import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String patientCnp;

    private String name;

    private String status;

    private String approval_by;

    private String approval_date;

    private List<MedicalCaseFieldDTO> fields;

    private List<MedicalCaseAttachmentDTO> attachments;

    public static MedicalCaseDTO build(MedicalCase mc) {
        return builder()
            .id(mc.getId())
            .fields(Optional.ofNullable(mc.getFields())
                .orElse(Collections.emptySet()).stream()
                .map(MedicalCaseFieldDTO::build)
                .collect(Collectors.toList()))
            .attachments(Optional.ofNullable(mc.getAttachments())
                .orElse(Collections.emptyList()).stream()
                .map(MedicalCaseAttachmentDTO::build)
                .collect(Collectors.toList()))
            .uuid(mc.getUuid())
            .status(mc.getStatus())
            .registryUuid(mc.getRegistryUuid())
            .patientCnp(mc.getPatientCnp())
            .name(mc.getName())
            .approval_by(mc.getApprovalBy())
            .approval_date(mc.getApproval_date())
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
            .patientCnp(mcDTO.getPatientCnp())
            .uuid(mcDTO.getUuid())
            .registryUuid(mcDTO.getRegistryUuid())
            .status(mcDTO.getStatus())
            .name(mcDTO.getName())
            .approvalBy(mcDTO.getApproval_by())
            .approvalDate();
    }



}
