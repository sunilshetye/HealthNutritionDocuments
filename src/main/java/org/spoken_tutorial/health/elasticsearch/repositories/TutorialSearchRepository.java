package org.spoken_tutorial.health.elasticsearch.repositories;

import org.spoken_tutorial.health.elasticsearch.models.TutorialSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TutorialSearchRepository extends ElasticsearchRepository<TutorialSearch, String> {

    TutorialSearch findByDocumentTypeAndDocumentId(String documentType, String documentTypeId);

}
