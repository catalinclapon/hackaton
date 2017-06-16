package com.db.hackaton.repository;

import com.db.hackaton.domain.Registry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Registry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryRepository extends JpaRepository<Registry,Long> {
    @Modifying
    @Query("update Registry r set r.status = ?1 where r.uuid = ?2")
    int setStatusForRegistries(String status, String uuid);

    Page<Registry> findByStatus(Pageable var1, String status);
}
