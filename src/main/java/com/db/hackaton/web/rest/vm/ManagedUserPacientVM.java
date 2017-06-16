package com.db.hackaton.web.rest.vm;

import com.db.hackaton.service.dto.UserDTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

/**
 * View Model for user and pacient info from UI
 */
public class ManagedUserPacientVM {

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLangKey() {
        return langKey;
    }

    public boolean isPacient() {
        return isPacient;
    }

    public String getCnp() {
        return cnp;
    }

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 2, max = 5)
    private String langKey;

    private boolean isPacient;

    private String cnp;

    public ManagedUserPacientVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserPacientVM(String firstName, String lastName, String email, String langKey, Boolean isPacient, String cnp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.isPacient = isPacient;
        this.cnp = cnp;
    }

    @Override
    public String toString() {
        return "ManagedUserPacientVM{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", langKey='" + langKey + '\'' +
            ", isPacient=" + isPacient +
            ", cnp='" + cnp + '\'' +
            '}';
    }
}
