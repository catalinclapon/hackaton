package com.db.hackaton.repository.search;

import com.db.hackaton.domain.RegistryField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RegistryField entity.
 */
public interface RegistryFieldSearchRepository extends ElasticsearchRepository<RegistryField, Long> {
}
