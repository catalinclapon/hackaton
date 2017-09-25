package com.db.hackaton.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
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

    @Column(name = "created_date")
    private Instant createdDate;

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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MedicalCaseAttachment)) return false;

        MedicalCaseAttachment that = (MedicalCaseAttachment) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(title, that.title)
            .append(location, that.location)
            .append(medicalCase, that.medicalCase)
            .append(createdDate, that.createdDate)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(title)
            .append(location)
            .append(medicalCase)
            .append(createdDate)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "MedicalCaseAttachment{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", location='" + location + '\'' +
            ", medicalCase=" + medicalCase +
            ", createdDate=" + createdDate +
            '}';
    }

    public MedicalCaseAttachment id(Long id) {
        setId(id);
        return this;
    }
}
