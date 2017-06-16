package com.db.hackaton.repository;

import com.db.hackaton.domain.MedicalCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the MedicalCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalCaseRepository extends JpaRepository<MedicalCase,Long> {

    List<MedicalCase> findByStatusAndRegistryUuid(String status, String registryUuid);
}
