package com.health.elastic.search.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.health.elastic.search.model.TutorialSearch;

public interface TutorialSearchRepository extends ElasticsearchRepository<TutorialSearch, String>{
	
	
	TutorialSearch findByDocumentTypeAndDocumentTypeIdAllIgnoreCase(String documentType, String documentTypeId);
	
	//TutorialSearch findTopByOrderByIdDesc();
}
