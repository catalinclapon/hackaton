package com.db.hackaton.repository;

import com.db.hackaton.domain.MedicalCaseAttachment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MedicalCaseAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalCaseAttachmentRepository extends JpaRepository<MedicalCaseAttachment,Long> {

}
