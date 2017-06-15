package com.db.hackaton.repository.search;

import com.db.hackaton.domain.Groups;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Groups entity.
 */
public interface GroupsSearchRepository extends ElasticsearchRepository<Groups, Long> {
}
