package com.db.hackaton.repository;

import com.db.hackaton.domain.MedicalCase;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


/**
 * Spring Data JPA repository for the MedicalCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalCaseRepository extends JpaRepository<MedicalCase,Long> {

    @Query("select medicalCase from MedicalCase medicalCase join MedicalCaseField mcField where mcField.field.name = 'CNP' and mcField.value = :cnp")
    MedicalCase findByCNP(@Param("cnp") String cnp);

    @Query("select medicalCase " +
        "from MedicalCase medicalCase JOIN Registry r ON r.uuid = medicalCase.registryUuid " +
        "where medicalCase.patientCnp = :cnp " +
        "and r.id = :registryId")
    List<MedicalCase> findByRegistryIdAndCNP(@Param("registryId") Long registryId, @Param("cnp") String cnp);

    List<MedicalCase> findByStatusAndRegistryUuid(String status, String registryUuid);

    @Query("select medicalCase from MedicalCase medicalCase JOIN Registry r ON r.uuid = medicalCase.registryUuid " +
        "WHERE medicalCase.lastModifiedDate = " +
        "(select MAX(mc.lastModifiedDate) from MedicalCase mc WHERE medicalCase.patientCnp = mc.patientCnp)" +
        "AND medicalCase.registryUuid = :registryUuid")
    List<MedicalCase> findByLatestModifiedDateAndRegistryUuid(@Param("registryUuid") String registryUuid);

    @Query("select medicalCase from MedicalCase medicalCase JOIN Registry r ON r.uuid = medicalCase.registryUuid " +
        "WHERE medicalCase.lastModifiedDate = " +
        "(select MAX(mc.lastModifiedDate) from MedicalCase mc WHERE medicalCase.patientCnp = mc.patientCnp)" +
        "AND r.id = :registryId AND medicalCase.patientCnp = :cnp")
    List<MedicalCase> findByLatestModifiedDateAndRegistryIdAndCnp(@Param("registryId") Long registryId, @Param("cnp") String cnp);
}
