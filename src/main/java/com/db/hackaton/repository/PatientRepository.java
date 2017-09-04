package com.db.hackaton.repository;

import com.db.hackaton.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select patient from Patient patient join User user on patient.user.id = user.id where patient.user in " +
        "(select user from User user join UserGroup userGroup on user.id = userGroup.user.id where userGroup.group.id = :groupId)")
    List<Patient> findAllByGroupId(@Param("groupId") Long groupId);
}
