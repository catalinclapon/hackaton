package com.db.hackaton.repository.search;

import com.db.hackaton.domain.MedicalCase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MedicalCase entity.
 */
public interface MedicalCaseSearchRepository extends ElasticsearchRepository<MedicalCase, Long> {
}
