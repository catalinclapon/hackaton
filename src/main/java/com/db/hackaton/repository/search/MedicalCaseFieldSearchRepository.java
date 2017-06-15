package com.db.hackaton.repository.search;

import com.db.hackaton.domain.MedicalCaseField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MedicalCaseField entity.
 */
public interface MedicalCaseFieldSearchRepository extends ElasticsearchRepository<MedicalCaseField, Long> {
}
