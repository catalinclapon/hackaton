package com.db.hackaton.repository;

import com.db.hackaton.domain.RegistryField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the RegistryField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryFieldRepository extends JpaRepository<RegistryField,Long> {
    List<RegistryField> findAllByRegistryId(Long registryId);
}
