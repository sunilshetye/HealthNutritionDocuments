package org.spoken_tutorial.health.elasticsearch.repositories;

import org.spoken_tutorial.health.elasticsearch.models.DocumentSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DocumentSearchRepository extends ElasticsearchRepository<DocumentSearch, String> {

    DocumentSearch findByDocumentTypeAndDocumentId(String documentType, String documentTypeId);

}
