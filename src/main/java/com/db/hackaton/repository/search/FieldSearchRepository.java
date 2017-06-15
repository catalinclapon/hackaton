package com.db.hackaton.repository.search;

import com.db.hackaton.domain.Field;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Field entity.
 */
public interface FieldSearchRepository extends ElasticsearchRepository<Field, Long> {
}
