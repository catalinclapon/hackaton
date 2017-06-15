package com.db.hackaton.repository.search;

import com.db.hackaton.domain.MedicalCaseAttachment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MedicalCaseAttachment entity.
 */
public interface MedicalCaseAttachmentSearchRepository extends ElasticsearchRepository<MedicalCaseAttachment, Long> {
}
