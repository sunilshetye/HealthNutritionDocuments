package org.spoken_tutorial.health.elasticsearch.services;

import org.spoken_tutorial.health.elasticsearch.repositories.TutorialSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
