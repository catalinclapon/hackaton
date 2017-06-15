package com.db.hackaton.domain;

import com.db.hackaton.service.dto.FieldDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Registry.
 */
@Entity
@Table(name = "registry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "registry")
public class Registry extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "registry", targetEntity = RegistryField.class)
    private Set<RegistryField> fields = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Registry id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Registry name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Registry desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUuid() {
        return uuid;
    }

    public Registry uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public Registry status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<RegistryField> getFields() {
        return fields;
    }

    public Registry fields(Set<RegistryField> fields) {
        this.fields = fields;
        return this;
    }

    public void setFields(Set<RegistryField> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Registry registry = (Registry) o;
        if (registry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Registry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
