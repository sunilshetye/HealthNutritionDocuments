package org.spoken_tutorial.health.elasticsearch.services;

import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueManagementService {
	
	@Autowired
	private QueueManagementRepository repo;
	
	public long getNewId() {
	       
	        try {
	            return repo.getNewId() + 1;
	        } catch (Exception e) {
	           
	            e.printStackTrace();
	            return 1;
	        }
	    }

}
