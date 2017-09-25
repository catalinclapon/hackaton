package com.db.hackaton.service.dto;


import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MedicalCaseAttachmentDTO {

    private Long id;
    private String title;
    private String location;
    private Long medicalCaseId;

    public static MedicalCaseAttachmentDTO build(MedicalCaseAttachment mca) {
        return builder()
            .id(mca.getId())
            .title(mca.getTitle())
            .medicalCaseId(Optional.ofNullable(mca.getMedicalCase().getId()).orElse(null))
            .build();
    }

    public static MedicalCaseAttachment build(MedicalCaseAttachmentDTO mcaDTO) {
        MedicalCaseAttachment mca = new MedicalCaseAttachment()
            .id(mcaDTO.getId())
            .title(mcaDTO.getTitle())
            .location(mcaDTO.getLocation());

        if (mcaDTO.getMedicalCaseId() != null) {
            mca.setMedicalCase(new MedicalCase());
            mca.getMedicalCase().setId(mcaDTO.getMedicalCaseId());
        }

        return mca;
    }

    @Override
    public String toString() {
        return "MedicalCaseAttachmentDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", location='" + location + '\'' +
            ", medicalCaseId=" + medicalCaseId +
            '}';
    }
}
