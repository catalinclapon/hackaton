package com.db.hackaton.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MedicalCaseAttachment.
 */
@Entity
@Table(name = "medical_case_attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicalcaseattachment")
public class MedicalCaseAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @ManyToOne
    private MedicalCase medicalCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public MedicalCaseAttachment title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public MedicalCaseAttachment location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MedicalCase getMedicalCase() {
        return medicalCase;
    }

    public MedicalCaseAttachment medicalCase(MedicalCase medicalCase) {
        this.medicalCase = medicalCase;
        return this;
    }

    public void setMedicalCase(MedicalCase medicalCase) {
        this.medicalCase = medicalCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicalCaseAttachment medicalCaseAttachment = (MedicalCaseAttachment) o;
        if (medicalCaseAttachment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalCaseAttachment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalCaseAttachment{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
