package com.db.hackaton.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RegistryField.
 */
@Entity
@Table(name = "registry_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "registryfield")
public class RegistryField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Registry registry;

    @ManyToOne(fetch = FetchType.LAZY)
    private Field field;

    public Long getId() {
        return id;
    }

    public RegistryField id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public RegistryField order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCategory() {
        return category;
    }

    public RegistryField category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Registry getRegistry() {
        return registry;
    }

    public RegistryField registry(Registry registry) {
        this.registry = registry;
        return this;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Field getField() {
        return field;
    }

    public RegistryField field(Field field) {
        this.field = field;
        return this;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistryField registryField = (RegistryField) o;
        if (registryField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registryField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegistryField{" +
            "id=" + getId() +
            ", order='" + getOrder() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
