package com.db.hackaton.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MedicalCaseField.
 */
@Entity
@Table(name = "medical_case_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicalcasefield")
public class MedicalCaseField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_value")
    private String value;

    @ManyToOne
    private Field field;

    @JsonIgnore
    @ManyToOne
    private MedicalCase medicalCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public MedicalCaseField value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public MedicalCaseField field(Field field) {
        this.field = field;
        return this;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public MedicalCase getMedicalCase() {
        return medicalCase;
    }

    public MedicalCaseField medicalCase(MedicalCase medicalCase) {
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
        MedicalCaseField medicalCaseField = (MedicalCaseField) o;
        if (medicalCaseField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalCaseField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalCaseField{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
