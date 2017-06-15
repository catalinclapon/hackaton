package com.db.hackaton.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Groups entity.
 */
public class GroupsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupsDTO groupsDTO = (GroupsDTO) o;
        if(groupsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
