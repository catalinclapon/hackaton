package com.db.hackaton.repository.search;

import com.db.hackaton.domain.UserGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserGroup entity.
 */
public interface UserGroupSearchRepository extends ElasticsearchRepository<UserGroup, Long> {
}
