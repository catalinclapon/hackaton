package com.db.hackaton.repository;

import com.db.hackaton.domain.RegistryField;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RegistryField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryFieldRepository extends JpaRepository<RegistryField,Long> {

}
