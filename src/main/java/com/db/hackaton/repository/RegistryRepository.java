package com.db.hackaton.repository;

import com.db.hackaton.domain.Registry;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Registry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryRepository extends JpaRepository<Registry,Long> {

}
