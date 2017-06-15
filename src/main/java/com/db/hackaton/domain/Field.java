package com.db.hackaton.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Field.
 */
@Entity
@Table(name = "field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "field")
public class Field extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @Column(name = "jhi_min")
    private Integer min;

    @Column(name = "required")
    private String required;

    @Column(name = "jhi_max")
    private Integer max;

    @Column(name = "ext_validation")
    private String extValidation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Field name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Field desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public Field type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMin() {
        return min;
    }

    public Field min(Integer min) {
        this.min = min;
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getRequired() {
        return required;
    }

    public Field required(String required) {
        this.required = required;
        return this;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Integer getMax() {
        return max;
    }

    public Field max(Integer max) {
        this.max = max;
        return this;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getExtValidation() {
        return extValidation;
    }

    public Field extValidation(String extValidation) {
        this.extValidation = extValidation;
        return this;
    }

    public void setExtValidation(String extValidation) {
        this.extValidation = extValidation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Field field = (Field) o;
        if (field.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), field.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Field{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", type='" + getType() + "'" +
            ", min='" + getMin() + "'" +
            ", required='" + getRequired() + "'" +
            ", max='" + getMax() + "'" +
            ", extValidation='" + getExtValidation() + "'" +
            "}";
    }
}
