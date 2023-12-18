package com.health.elastic.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.health.elastic.search.repository.TutorialSearchRepository;

@Service
public class TutorialSearchService {
	@Autowired
	TutorialSearchRepository repo;
	
	/*public int getNewId() {
		
		try {
			return repo.findTopByOrderByIdDesc().getId()+1;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	*/

}
