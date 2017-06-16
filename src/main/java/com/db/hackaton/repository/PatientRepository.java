package com.db.hackaton.repository;

import com.db.hackaton.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    @Query("select patient from Patient patient where patient.user.login = ?#{principal.username}")
    List<Patient> findByUserIsCurrentUser();

    Patient findFirstByCnp(String cnp);
}
