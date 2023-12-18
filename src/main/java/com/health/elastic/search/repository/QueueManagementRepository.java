package com.health.elastic.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.health.elastic.search.model.OueueManagement;

public interface QueueManagementRepository  extends ElasticsearchRepository<OueueManagement, Integer>{

}
