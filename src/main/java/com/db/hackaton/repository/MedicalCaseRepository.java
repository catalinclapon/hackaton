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

    @Query("select medicalCase "+
            "from MedicalCase medicalCase JOIN Registry r ON r.uuid = medicalCase.registryUuid  "+
            "where medicalCase.patientCnp = :cnp "+
            "and medicalCase.registryUuid = :registryUuid")
    List<MedicalCase> findByRegistryUuidAndCNP(@Param("registryUuid") String registryUuid, @Param("cnp") String cnp);

    MedicalCase findById(Long id);

    List<MedicalCase> findByStatusAndRegistryUuid(String status, String registryUuid);




    @Query("select medicalCase from MedicalCase medicalCase JOIN Registry registry ON registry.uuid = medicalCase.registryUuid " +
        "WHERE medicalCase.registryUuid = :registryUuid " +
        "AND medicalCase.lastModifiedDate = " +
        "(select MAX(mc.lastModifiedDate) from MedicalCase mc JOIN Registry r ON r.uuid = mc.registryUuid " +
        "WHERE medicalCase.patientCnp = mc.patientCnp " +
        "AND mc.registryUuid = :registryUuid)")
    List<MedicalCase> findByLatestModifiedDateAndRegistryUuid(@Param("registryUuid") String registryUuid);

    @Query("select medicalCase from MedicalCase medicalCase JOIN Registry registry ON registry.uuid = medicalCase.registryUuid " +
        "WHERE registry.id = :registryId AND medicalCase.patientCnp = :cnp " +
        "AND medicalCase.lastModifiedDate = " +
        "(select MAX(mc.lastModifiedDate) from MedicalCase mc JOIN Registry r ON r.uuid = mc.registryUuid " +
        "WHERE medicalCase.patientCnp = mc.patientCnp " +
        "AND r.id = :registryId)")
    List<MedicalCase> findByLatestModifiedDateAndRegistryIdAndCnp(@Param("registryId") Long registryId, @Param("cnp") String cnp);

    List<MedicalCase> findByRegistryUuid(String registryUuid);

    @Modifying
    @Query("update MedicalCase medicalCase set medicalCase.status = :newStatus where medicalCase.id = :id")
    int setStatusForMedicalCase(@Param("newStatus") String newStatus, @Param("id") Long id);

    @Modifying
    @Query("update MedicalCase medicalCase set medicalCase.approval_by= :doctor where medicalCase.id= :id")
    int setAprovalBy(@Param("doctor") String doctor,@Param("id") Long id);

    @Modifying
    @Query("update MedicalCase medicalCase set medicalCase.approval_date=:approval_date where medicalCase.id=:id")
    int setApprovalDate(@Param("approval_date") String approval_date, @Param("id") Long id);

}
