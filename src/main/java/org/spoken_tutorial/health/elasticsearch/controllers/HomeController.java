package org.spoken_tutorial.health.elasticsearch.controllers;

import java.util.List;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.spoken_tutorial.health.elasticsearch.contentfile.ContentsfromFile;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.models.TutorialSearch;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.repositories.TutorialSearchRepository;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.spoken_tutorial.health.elasticsearch.services.TutorialSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {
	
	@Autowired
	TutorialSearchRepository tutorialRepo;
	
	@Autowired
	QueueManagementRepository queRepo;
	
	@Autowired
	QueueManagementService queuemntService;
	
	@Autowired
	TutorialSearchService tutSearchService;
	
	@PostMapping("/addDocument/{path}/{documentType}/{documentTypeId}")
	public int addDocument(@PathVariable String path, @PathVariable String documentType, @PathVariable String documentTypeId) {
		
		TutorialSearch tut= new TutorialSearch();
		
		Parser parser= new AutoDetectParser();
		String docContent="";
	
		try {
			docContent=ContentsfromFile.extractContentForElastic(parser, path);
			tut.setDocumentContent(docContent);
		}
		catch(Exception e) {
			
		}
		
		tut.setDocumentType(documentType);
		tut.setDocumentTypeId(documentTypeId);
		tutorialRepo.save(tut);
		return 1;
		
	}
	
	@PutMapping("/updateDocument/{path}/{documentType}/{documentTypeId}")
	public int updateDocument(@PathVariable String path,@PathVariable String documentType,@PathVariable String documentTypeId) {
		
		TutorialSearch tut = tutorialRepo.findByDocumentTypeAndDocumentTypeId(documentType, documentTypeId);
		Parser parser= new AutoDetectParser();
		String docContent="";
	
		try {
			docContent=ContentsfromFile.extractContentForElastic(parser, path);
			tut.setDocumentContent(docContent);
			
			tutorialRepo.save(tut);
		}
		catch(Exception e) {
			
		}
		
		
		
		return 1;
		
	}
	
	@GetMapping("/findByDocumentTypeAndDocumentTypeId/{documentType}/{documentTypeId}")
	public TutorialSearch findByDocumentTypeAndDocumentTypeId(@PathVariable String documentType,@PathVariable String documentTypeId) {
		TutorialSearch tut = tutorialRepo.findByDocumentTypeAndDocumentTypeId(documentType, documentTypeId);
		return tut;
	}
	
	
	
	@DeleteMapping("/deleteDocument/{id}")
	public String deleteDocument(@PathVariable String id) {	
		tutorialRepo.deleteById(id);
		return "deleted";
		
	}
	
	
	
	
	
	
	/************Testing ************************************************************/
	
	@PostMapping("/addDocumentTest/{content}/{documentType}/{documentTypeId}")
	public int addDocumentTest(@PathVariable String content, @PathVariable String documentType, @PathVariable String documentTypeId) {
		TutorialSearch tut= new TutorialSearch();
		//tut.setId(repo.findMaxId().getId());
		tut.setId("1");
		tut.setDocumentContent(content);
		tut.setDocumentType(documentType);
		tut.setDocumentTypeId(documentTypeId);
		tutorialRepo.save(tut);
		return 1;
		
	}
	

	@PutMapping("/updateDocumentTest/{content}/{documentType}/{documentTypeId}")
	public int updateDocumentTest(@PathVariable String content,@PathVariable String documentType,@PathVariable String documentTypeId) {
		
		TutorialSearch tut = tutorialRepo.findByDocumentTypeAndDocumentTypeId(documentType, documentTypeId);
		
		tut.setDocumentContent(content);
		
		tutorialRepo.save(tut);
		return 1;
		
	}
	
	@PostMapping("/saveQueueTest/{requestId}/{requestType}")
	QueueManagement saveQueueTest(@PathVariable String requestId, @PathVariable String requestType){
		QueueManagement q1= new QueueManagement();
		q1.setQueueId(queuemntService.getNewId());
		q1.setRequestId(requestId);
		q1.setRequestType(requestType);
		queRepo.save(q1);
		return q1;
		
	}
	

	@GetMapping("/findAllQueueTest")
	Iterable<QueueManagement> findAllQueueTest(){
	
		return  queRepo.findAll();
		
	}


}
