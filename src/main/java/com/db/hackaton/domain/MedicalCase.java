package com.db.hackaton.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.TreeSet;
import java.util.Objects;
import java.util.Set;

/**
 * A MedicalCase.
 */
@Entity
@Table(name = "medical_case")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicalcase")
public class MedicalCase extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    //@NotNull
    @Column(name = "uuid", nullable = true)
    private String uuid;

    //@NotNull
    @Column(name = "registry_uuid", nullable = true)
    private String registryUuid;

    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "patient_cnp")
    private String patientCnp;

    @Column(name="approval_by")
    private String approval_by;

    @Column (name="approval_date")
    private String approval_date;

    @OneToMany(mappedBy = "medicalCase", targetEntity = MedicalCaseField.class, fetch = FetchType.EAGER,
        cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<MedicalCaseField> fields = new TreeSet<>();


    public Long getId() {
        return id;
    }

    public MedicalCase id(Long id) {
        setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MedicalCase name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public MedicalCase uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public MedicalCase status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientCnp() {
        return patientCnp;
    }

    public MedicalCase patientCnp(String patientCnp) {
        this.patientCnp = patientCnp;
        return this;
    }

    public void setPatientCnp(String patientCnp) {
        this.patientCnp = patientCnp;
    }

    public Set<MedicalCaseField> getFields(){
        return fields;
    }

    public MedicalCase fields(Set<MedicalCaseField> fields){
        this.fields = fields;
        return this;
    }

    public void setFields(Set<MedicalCaseField> fields){
        this.fields = fields;
    }

    public String getApprovalBy(){
        return approval_by;
    }

    public void setApprovalBy(String approval_by){
        this.approval_by=approval_by;
    }

    public MedicalCase approvalBy(String approval_by){
        setApprovalBy(approval_by);
        return this;
    }

    public String getApproval_date(){
        return approval_date;
    }

    public void setApprovalDate(String approval_date){
    this.approval_date=approval_date;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicalCase medicalCase = (MedicalCase) o;
        if (medicalCase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalCase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalCase{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

    public String getRegistryUuid() {
        return registryUuid;
    }

    public MedicalCase registryUuid(String registryUuid) {
        this.registryUuid = registryUuid;
        return this;
    }

    public void setRegistryUuid(String registryUuid) {
        this.registryUuid = registryUuid;
    }
}
