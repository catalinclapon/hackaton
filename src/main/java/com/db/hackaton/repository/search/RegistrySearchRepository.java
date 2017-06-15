package com.db.hackaton.repository.search;

import com.db.hackaton.domain.Registry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Registry entity.
 */
public interface RegistrySearchRepository extends ElasticsearchRepository<Registry, Long> {
}
