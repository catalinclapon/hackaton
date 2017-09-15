package com.db.hackaton.repository;

import com.db.hackaton.domain.MedicalCaseField;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MedicalCaseField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalCaseFieldRepository extends JpaRepository<MedicalCaseField,Long> {

    void deleteById(Long id);

}
