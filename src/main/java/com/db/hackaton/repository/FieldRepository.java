package com.db.hackaton.repository;

import com.db.hackaton.domain.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Field entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldRepository extends JpaRepository<Field,Long> {
    Field findFirstByName(String name);

    Field findById(Long id);

}
